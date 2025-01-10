package org.briesearch.tokenmasks.tokenizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * @author Brissach
 * @since 09.01.2025 14:58
 * © token-masks - All Rights Reserved
 */
public class TokenizerImpl extends AbstractTokenizer implements Tokenizer {

  private final Map<String, Integer> indices = new HashMap<>();

  public TokenizerImpl(TokenizerBuilder builder) {
    super(builder);
  }

  /**
   * Tokenizes the input text by splitting it into tokens based on defined delimiters.
   * Optionally, the tokens are converted to lowercase if the lowercaseTokens flag is set.
   * It also performs recursive token splitting if a token is not found in the indices map.
   *
   * @param text The input text to tokenize.
   * @return A list of tokens extracted from the input text.
   */
  @Override
  public List<String> tokenize(String text) {
    if (lowercaseTokens)
      text = text.toLowerCase();

    List<String> tokens = new ArrayList<>();
    Matcher matcher = delimiter.matcher(text);
    int previousEnd = 0;

    while (matcher.find()) {
      if (matcher.start() > previousEnd) {
        String token = text.substring(
          previousEnd,
          matcher.start()
        ).trim();

        if (!token.isEmpty()) {
          if (indices.containsKey(token))
            tokens.add(token);
          else
            recursiveSplitTokenSearch(
              tokens,
              token.split("\\s+")
            );
        }
      }

      String delimiter = matcher.group();
      tokens.add(delimiter);

      previousEnd = matcher.end();
    }

    if (previousEnd < text.length()) {
      String lastToken = text.substring(previousEnd).trim();
      if (!lastToken.isEmpty())
        recursiveSplitTokenSearch(
          tokens,
          lastToken.split("\\s+")
        );
    }

    if (fitUponTokenization)
      fit(tokens);

    return tokens;
  }

  /**
   * Recursively attempts to split a token into smaller sub-tokens if the original token is not found in the indices map.
   * The splitting process continues until a match is found or no further split is possible.
   *
   * @param tokens The list to which the valid tokens will be added.
   * @param split The array of sub-tokens derived from attempting to split the original token.
   */
  void recursiveSplitTokenSearch(List<String> tokens, String[] split) {
    for (String tok : split) {
      int length = tok.length();
      int recursiveIt = length;

      boolean visited = false;
      while (recursiveIt > 0) {
        String subToken = tok.substring(0, recursiveIt);
        if (indices.containsKey(subToken)) {
          tokens.add(subToken);
          visited = true;
          if (recursiveIt < length)
            tokens.add(
              tok.substring(
                recursiveIt,
                length
              ));
          break;
        }
        recursiveIt--;
      }

      if (!visited)
        tokens.add(tok);
    }
  }

  /**
   * Retrieves the index of a specific token from the tokenizers indices.
   * If the token is not found, returns -1.
   *
   * @param token The token whose index is to be retrieved.
   * @return The index of the token, or -1 if the token is not found.
   */
  @Override
  public int indexOf(String token) {
    if (lowercaseTokens)
      token = token.toLowerCase();

    return indices.getOrDefault(token, -1);
  }

  /**
   * Retrieves the word associated with a specific index in the tokenizers indices map.
   * If no word is found for the given index, returns null.
   *
   * @param index The index whose associated word is to be retrieved.
   * @return The word corresponding to the index, or null if not found.
   */
  @Override
  public String wordOf(int index) {
    for (Map.Entry<String, Integer> entry : indices.entrySet()) {
      if (entry.getValue() == index) return entry.getKey();
    }
    return null;
  }

  /**
   * Fits a single token to the tokenizer by adding it to the indices map if it is not already present.
   * Optionally converts the token to lowercase before adding it, depending on the tokenizer settings.
   *
   * @param token The token to be added to the tokenizer's index.
   */
  @Override
  public void fit(String token) {
    token = lowercaseTokens ? token.toLowerCase() : token;
    indices.putIfAbsent(
      token,
      indices.size()
    );
  }

  @Override
  public long countTotalCharacters() {
    return indices.keySet()
      .stream()
      .mapToLong(String::length)
      .sum();
  }

  @Override
  public long countCharacters() {
    Set<Character> chars = new HashSet<>();
    indices.keySet()
      .forEach(k -> {
        char[] arr = k.toCharArray();
        for (char c : arr) chars.add(c);
      });
    return chars.size();
  }

  @Override
  public Map<String, Integer> asMap() {
    return new HashMap<>(indices);
  }
}
