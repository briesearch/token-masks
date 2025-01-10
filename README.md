# Token Masks

This is a machine learning experiment that leverages a deep neural network to predict masked words in sentences. The model uses a simple custom tokenizer and generates predictions for text with a "MASK" token, allowing you to easily substitute the masked word.

## Features
- **Tokenization**: Supports custom tokenization for handling input text with a "MASK" token.
- **Multi-Layer Neural Network**: A multi-layer neural network with a softmax output layer to predict the token for the MASK.
- **Positional Encoding**: Incorporates positional encoding to add sequence order information to the token embeddings, helping the model understand the relative position of words in a sentence. This enables more accurate predictions for the masked word by maintaining the order of words in the sequence.
- **Random Sampling**: Allows random sampling of predictions using a `temperature` parameter to control prediction randomness (from deterministic predictions at temperature = 1.0 to more diverse predictions as the temperature approaches 0).
- **Training**: Provides an option to train the neural network with custom samples to improve predictions.

## Requirements
- Java 8 or higher
- [Aurora](https://github.com/AcaiSoftware/aurora) for neural network operations - 1.0.1 version

## Installation

### Clone the repository
```bash
git clone https://github.com/briesearch/token-masks.git
cd token-masks
```

## Tests

### Setup tokenizer
```java
Tokenizer tokenizer = new TokenizerBuilder()
  .build();
```

### Create samples (Key-Value of masked sentence and its target)
```java
Map<String, String> samples = new HashMap<>();
samples.put("hello how MASK you", "are");
samples.put("what are you MASK", "doing");
samples.put("I MASK my coffee black", "prefer");
```

### Setup model
```java
Model model = new Model(
  tokenizer,
  samples,
  new Kwargs(
    "n_vocab", 32
  ));

model.construct(); // initializes neural network
```

### Train model
```java
model.forward(samples);
```

### Test Showcase
```java
double temperature = 1.0; // 1.0 refers to the best token, lowering the temperature introduces random sampling
samples.forEach((k, v) -> {
  System.out.println("Sample: " + k + " -> " + model.generate(k, temperature));
});
```
```
Epoch: 456 Loss: 0.02986378142952657
Sample: I feel MASK today -> I feel happy today
Sample: I MASK my coffee black -> I prefer my coffee black
Sample: the MASK is very bright -> the moon is very bright
Sample: he MASK the painting carefully -> he admired the painting carefully
Sample: he MASK the ball quickly -> he caught the ball quickly
Sample: the MASK makes me laugh -> the joke makes me laugh
Sample: we MASK the mountain yesterday -> we climbed the mountain yesterday
Sample: she is MASK the book -> she is reading the book
Sample: can you MASK this word -> can you fix this word
Sample: it is MASK outside -> it is cold outside
Sample: the MASK is delicious -> the cake is delicious
```
