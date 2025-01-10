package org.briesearch.tokenmasks.tokenizer;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @author Brissach
 * @since 09.01.2025 14:57
 * © token-masks - All Rights Reserved
 */
public interface Tokenizer {

  Pattern DELIMITER = compile(
    "[()?!.,:;*+\\-/\\\\|\\[\\]{}<>=~&^%$#@`\"'\\d]"
  );

  List<String> tokenize(String text);

  int indexOf(String token);

  String wordOf(int index);

  default void fit(Iterable<String> tokens) {
    tokens.forEach(this::fit);
  }

  default void fit(String... tokens) {
    for (String token : tokens) fit(token);
  }

  void fit(String token);

  long countTotalCharacters();

  long countCharacters();

  Map<String, Integer> asMap();

}