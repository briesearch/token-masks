package org.briesearch.tokenmasks;

import org.briesearch.tokenmasks.tokenizer.Tokenizer;

import java.util.List;
import java.util.Map;

/**
 * OneHotEncoder is an implementation of the Encoder interface that encodes text into a one-hot numerical representation.
 * Each token in the input text is represented as a vector where the corresponding index is set to 1, and all other indices are 0.
 * This encoding scheme is useful for representing discrete tokens in a format suitable for neural networks.
 *
 * @author Brissach
 * @since 09.01.2025 16:36
 * © token-masks - All Rights Reserved
 */
public class OneHotEncoder implements Encoder {
  @Override
  public double[] encode(Tokenizer tokenizer, String text, int n_vocab) {
    double[] vector = new double[tokenizer.asMap().size()];
    List<String> tokens = tokenizer.tokenize(text);
    Map<String, Integer> tokenMap = tokenizer.asMap();
    for (String token : tokens) {
      if (!tokenMap.containsKey(token)) {
        continue;
      }
      vector[tokenizer.indexOf(token)] = 1;
    }
    return vector;
  }
}
