package org.briesearch.tokenmasks;

import com.google.common.base.Preconditions;
import io.arctis.aurora.algebraic.ContextVector;
import io.arctis.aurora.model.ActivationFunction;
import io.arctis.aurora.ops.Kwargs;
import io.arctis.aurora_deep.BuildLayer;
import io.arctis.aurora_deep.MultiLayerNeuralNetBuilder;
import io.arctis.aurora_deep.MultiLayerNeuralNetwork;
import org.briesearch.tokenmasks.tokenizer.Tokenizer;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model is responsible for generating masked samples, training a neural network to learn from those samples,
 * and generating predictions using the trained model. The model uses tokenization and encoding techniques for
 * processing text data, and provides functionality for forward propagation, model construction, and sample generation.
 *
 * @author Brissach
 * @since 09.01.2025 14:57
 * © token-masks - All Rights Reserved
 */
public class Model {

  public static final String MASK = "MASK";

  private final Encoder
    positional = new PositionalEncoder(),
    onehot = new OneHotEncoder();

  private final Tokenizer tokenizer;
  private MultiLayerNeuralNetwork model;
  private final int n_vocab;
  private final int n_tokens;
  private final long seed;

  /**
   * Constructs a Model instance using the provided tokenizer, samples, and additional parameters.
   * It fits the tokenizer with the given tokens and masks from the samples.
   *
   * @param tokenizer The tokenizer used to tokenize input text.
   * @param samples A map of sample sentences and their corresponding masks.
   * @param kwargs Additional keyword arguments (e.g., vocabulary size, seed).
   */
  public Model(Tokenizer tokenizer, Map<String, String> samples, Kwargs kwargs) {
    tokenizer.fit(MASK);

    for (Map.Entry<String, String> entry : samples.entrySet()) {
      String tokens = entry.getKey();
      String mask = entry.getValue();
      tokenizer.fit(tokenizer.tokenize(tokens));
      tokenizer.fit(tokenizer.tokenize(mask));
    }

    n_tokens = tokenizer.asMap().size();
    this.n_vocab = kwargs.get("n_vocab", 32);
    this.seed = kwargs.get("seed", 24L);
    this.tokenizer = tokenizer;
  }

  /**
   * Generates masked samples for the given list of sentences.
   *
   * @param sentences A list of sentences for which to generate samples.
   * @return A map where the keys are original sentences, and values are the corresponding masked sentences.
   */
  @Nonnull
  public Map<String, String> generateSamples(@Nonnull List<String> sentences) {
    SentenceMasker masker = new SentenceMasker(seed, MASK);
    Map<String, String> samples = new HashMap<>();
    sentences.forEach(sentence -> {
      String[] masked = masker.mask(sentence);
      samples.put(masked[0], masked[1]);
    });
    return samples;
  }

  /**
   * Constructs the model using the provided neural network.
   *
   * @param model The neural network model to be used for the task.
   * @return The current Model instance with the given neural network.
   */
  @Nonnull
  public Model construct(MultiLayerNeuralNetwork model) {
    this.model = model;
    return this;
  }

  @Nonnull
  public Model construct(MultiLayerNeuralNetBuilder model) {
    this.model = model
      .seed(seed)
      .inputSize(n_vocab * (n_tokens + 1))
      .build();
    return this;
  }

  /**
   * Constructs the model by creating a new MultiLayerNeuralNetwork instance.
   * The network configuration includes an input size, a hidden layer, and an output layer.
   *
   * @return The current Model instance with the newly created neural network.
   */
  @Nonnull
  public Model construct() {
    return construct(
      MultiLayerNeuralNetwork.configureContexts()
        .seed(seed)
        .inputSize(n_vocab * (n_tokens + 1))
        .addLayer(new BuildLayer()
          .size(32)
          .activation(ActivationFunction.ARCTAN))
        .outputLayer(new BuildLayer()
          .size(n_tokens)
          .activation(ActivationFunction.SOFTMAX))
        .build());
  }

  /**
   * Performs forward propagation on the model using the given samples.
   * This method trains the model with the samples until the loss is below the given preference value.
   *
   * @param samples A map of input-output pairs (tokens and corresponding masks) for training.
   * @param alpha The learning rate for training.
   * @param preference The threshold for stopping the training (based on loss).
   */
  public void forward(Map<String, String> samples, double alpha, double preference) {
    int size = samples.size();
    double[][] inputs = new double[size][];
    double[][] outputs = new double[size][];
    int i = 0;

    for (Map.Entry<String, String> entry : samples.entrySet()) {
      inputs[i] = positional.encode(tokenizer, entry.getKey(), n_vocab);
      outputs[i] = onehot.encode(tokenizer, entry.getValue(), -1);
      i++;
    }

    System.out.println("Shape: " + inputs[0].length);

    double loss = 1.0;
    int epoch = 0;
    while (loss > preference) {
      model.train(inputs, outputs, alpha);
      loss = model.loss(inputs, outputs);
      epoch++;
      System.out.println("Epoch: " + epoch + " Loss: " + loss);
    }
  }

  /**
   * Default forward propagation with standard values for alpha and preference.
   *
   * @param samples A map of input-output pairs (tokens and corresponding masks) for training.
   */
  public void forward(Map<String, String> samples) {
    forward(samples, 0.1, 0.03);
  }

  /**
   * Generates a string by replacing the MASK token in the input text with a predicted token.
   * The prediction is made using the trained model and optionally controlled by temperature.
   *
   * @param text The input text containing the MASK token.
   * @param temperature The temperature controlling the randomness of the prediction.
   * @return The generated text with the MASK token replaced by the predicted token.
   * @throws IllegalStateException If the input text does not contain the MASK token.
   */
  @Nonnull
  public String generate(@Nonnull String text, double temperature)
    throws IllegalStateException {

    Preconditions.checkState(
      text.contains(MASK),
      "Input text must contain the MASK token. Mask token index for context: " + tokenizer.indexOf(MASK)
    );

    double[] inputVector = positional
      .encode(
        tokenizer,
        text,
        n_vocab
      );

    ContextVector outputProbabilities = ContextVector.vector(model.predict(inputVector));
    int tokenIndex = temperature == 1.0
      ? outputProbabilities.argmax()
      : randomSampleToken(outputProbabilities, temperature);

    String bestToken = tokenizer.wordOf(tokenIndex);

    Preconditions.checkNotNull(
      bestToken,
      "Best token index not found in tokenizer."
    );

    return text.replace(MASK, bestToken);
  }

  /**
   * Samples a token index based on the output probabilities, adjusted for temperature.
   *
   * @param outputProbabilities The output probabilities for each token.
   * @param temperature The temperature value controlling the randomness of the sampling.
   * @return The index of the randomly sampled token.
   */
  public int randomSampleToken(ContextVector outputProbabilities, double temperature) {
    double[] probabilities = outputProbabilities.transform();

    for (int i = 0; i < probabilities.length; i++)
      probabilities[i] = Math.pow(probabilities[i], 1.0 / temperature);

    double sum = Arrays.stream(probabilities).sum();
    for (int i = 0; i < probabilities.length; i++)
      probabilities[i] /= sum;

    double randomValue = Math.random();
    double cumulativeProbability = 0.0;

    for (int i = 0; i < probabilities.length; i++) {
      cumulativeProbability += probabilities[i];
      if (randomValue <= cumulativeProbability) {
        return i;
      }
    }

    // Should never happen
    return outputProbabilities.argmax();
  }

}
