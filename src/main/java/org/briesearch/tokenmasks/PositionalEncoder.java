package org.briesearch.tokenmasks;

import io.arctis.aurora.algebraic.ContextVector;
import org.briesearch.tokenmasks.tokenizer.Tokenizer;

import java.util.List;

/**
 * PositionalEncoder is an implementation of the Encoder interface that encodes text into a numerical representation
 * using a combination of one-hot encoding for token indices and positional encoding for the position of tokens in the sequence.
 * This encoding scheme is used for converting textual data into a format suitable for neural network processing.
 *
 * @author Brissach
 * @since 09.01.2025 16:33
 * © token-masks - All Rights Reserved
 */
public class PositionalEncoder implements Encoder {
  @Override
  public double[] encode(Tokenizer tokenizer, String text, int n_vocab) {
    List<String> tokens = tokenizer.tokenize(text);
    int vocabSize = tokenizer.asMap().size();
    int featureSize = vocabSize + 1; // vocab size + 1 position dimension
    int maxSize = n_vocab * featureSize; // total size of the flattened vector

    // flat vector for the sequence
    double[] vector = new double[tokens.size() * featureSize];

    for (int i = 0; i < tokens.size(); i++) {
      String token = tokens.get(i);
      int tokenIndex = tokenizer.indexOf(token);

      // one-hot for token index
      if (tokenIndex != -1) {
        vector[i * featureSize + tokenIndex] = 1;
      }

      // positional encoding as the last dimension
      vector[(i + 1) * featureSize - 1] = (double) i / tokens.size();
    }

    ContextVector ctx = ContextVector.vector(vector);
    return ctx.padSequence(maxSize).transform();
  }

}
