package org.briesearch.tokenmasks;

import org.briesearch.tokenmasks.tokenizer.Tokenizer;

/**
 * @author Brissach
 * @since 09.01.2025 16:28
 * © token-masks - All Rights Reserved
 */
public interface Encoder {
  /**
   * Encodes the input text into a numerical vector representation based on the specific encoding scheme implemented.
   *
   * @param tokenizer The tokenizer used to tokenize the input text and map tokens to indices.
   * @param text The input text to be encoded.
   * @param n_vocab The maximum number of tokens for which the encoding will be generated (may be used by implementations to determine the vector size).
   * @return A numerical vector representing the encoded version of the input text.
   */
  double[] encode(
    Tokenizer tokenizer,
    String text,
    int n_vocab
  );
}
