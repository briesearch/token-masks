package org.briesearch.tokenmasks.tokenizer;

import java.util.regex.Pattern;

/**
 * @author Brissach
 * @since 09.01.2025 14:58
 * © token-masks - All Rights Reserved
 */
public abstract class AbstractTokenizer {

  protected final Pattern delimiter;
  protected final boolean findNearestToken;
  protected final boolean allowRegistrations;
  protected final boolean lowercaseTokens;
  protected final boolean fitUponTokenization;
  protected final boolean simpleTokens;

  public AbstractTokenizer(TokenizerBuilder builder) {
    delimiter = builder.delimiter;
    findNearestToken = builder.findNearestToken;
    allowRegistrations = builder.allowRegistrations;
    lowercaseTokens = builder.lowercaseTokens;
    fitUponTokenization = builder.fitUponTokenization;
    simpleTokens = builder.simpleTokens;
  }
}