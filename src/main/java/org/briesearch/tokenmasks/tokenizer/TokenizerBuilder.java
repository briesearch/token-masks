package org.briesearch.tokenmasks.tokenizer;

import java.util.regex.Pattern;

/**
 * @author Brissach
 * @since 09.01.2025 14:58
 * © token-masks - All Rights Reserved
 */
public class TokenizerBuilder {

  protected Pattern delimiter = Tokenizer.DELIMITER;

  protected boolean findNearestToken;
  protected boolean allowRegistrations;
  protected boolean lowercaseTokens;
  protected boolean fitUponTokenization;
  protected boolean optTokens;
  protected boolean whitespacePlaceholder;
  protected boolean simpleTokens;

  public TokenizerBuilder includeUnseenTokens() {
    allowRegistrations = true;
    return this;
  }

  public TokenizerBuilder findNearest() {
    findNearestToken = true;
    return this;
  }

  public TokenizerBuilder lowercaseTokens() {
    lowercaseTokens = true;
    return this;
  }

  public TokenizerBuilder fitUponTokenization() {
    fitUponTokenization = true;
    return this;
  }

  public TokenizerBuilder optTokens() {
    optTokens = true;
    return this;
  }

  public TokenizerBuilder simpleTokens() {
    simpleTokens = true;
    return this;
  }

  /**
   * Enables placeholder tokens for whitespaces
   * <p>
   * Example:
   * <ul>
   *   <li>&lt;S(n)&gt; - n represents the amount of spaces (e.g. &lt;S(4)&gt; is 4 spaces)</li>
   *   <li>&lt;S&gt; - single space placeholder</li>
   * </p>
   * @return Returns this instance for chaining
   */
  public TokenizerBuilder whitespacePlaceholder() {
    whitespacePlaceholder = true;
    return this;
  }

  public TokenizerBuilder withDelimiter(Pattern delimiter) {
    this.delimiter = delimiter;
    return this;
  }

  public Tokenizer build(Class<? extends Tokenizer> type) {
    try {
      return type
        .getConstructor(TokenizerBuilder.class)
        .newInstance(this);
    } catch (Exception e) {
      throw new RuntimeException(
        "Tokenizer '" + type.getSimpleName() + "' requires a constructor with type TokenizerBuilder"
      );
    }
  }

  public Tokenizer build() {
    return build(TokenizerImpl.class);
  }

}
