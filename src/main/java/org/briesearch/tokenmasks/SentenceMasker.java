package org.briesearch.tokenmasks;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * SentenceMasker is a utility class that performs random masking of a single token within a sentence.
 * It replaces a randomly chosen token in the input sentence with a predefined mask token. This is typically used
 * for training models that need to predict or reconstruct the masked token.
 *
 * @author Brissach
 * @since 10.01.2025 05:44
 * © token-masks - All Rights Reserved
 */
public class SentenceMasker {

  private final Random random;
  private final String maskToken;

  public SentenceMasker(long seed, String maskToken) {
    this.random = new Random(seed);
    this.maskToken = maskToken;
  }

  /**
   * Masks a randomly selected token in the input sentence with the predefined mask token.
   * The original sentence is returned with the mask token replacing one randomly chosen token.
   *
   * @param text The input sentence to be masked.
   * @return An array containing two elements: the masked sentence and the original token that was replaced.
   */
  @Nonnull
  public String[] mask(@Nonnull String text) {
    String[] tokens = text.split(" ");
    int index = random.nextInt(tokens.length);
    String maskedToken = tokens[index];
    tokens[index] = maskToken;
    String masked = String.join(" ", tokens);
    return new String[]{masked, maskedToken};
  }

}
