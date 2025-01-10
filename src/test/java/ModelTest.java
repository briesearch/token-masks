import io.arctis.aurora.ops.Kwargs;
import org.briesearch.tokenmasks.Model;
import org.briesearch.tokenmasks.tokenizer.Tokenizer;
import org.briesearch.tokenmasks.tokenizer.TokenizerBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Brissach
 * @since 10.01.2025 06:17
 * © token-masks - All Rights Reserved
 */
public class ModelTest {
  public static void main(String[] args) {
    Tokenizer tokenizer = new TokenizerBuilder()
      .build();

    Map<String, String> samples = new HashMap<>();
    samples.put("hello how MASK you", "are");
    samples.put("what are you MASK", "doing");
    samples.put("this is a MASK example", "simple");
    samples.put("can you MASK me", "help");
    samples.put("I feel MASK today", "happy");
    samples.put("learning is MASK", "fun");
    samples.put("it is MASK outside", "cold");
    samples.put("the MASK is shining", "sun");
    samples.put("he MASK to the store", "went");
    samples.put("she is MASK the book", "reading");
    samples.put("they MASK at the park", "are");
    samples.put("MASK the lights off", "turn");
    samples.put("we MASK to the movies", "went");
    samples.put("it is MASK in here", "quiet");
    samples.put("this MASK smells nice", "flower");
    samples.put("MASK is your favorite color", "what");
    samples.put("he MASK the ball quickly", "caught");
    samples.put("she MASK the homework early", "finished");
    samples.put("can you MASK this for me", "fix");
    samples.put("MASK are you talking about", "what");
    samples.put("the MASK is delicious", "cake");
    samples.put("let us MASK the puzzle", "solve");
    samples.put("MASK did you go yesterday", "where");
    samples.put("this MASK looks amazing", "painting");
    samples.put("please MASK the dishes", "wash");
    samples.put("they MASK during the meeting", "spoke");
    samples.put("MASK to the party on time", "come");
    samples.put("she MASK the keys on the table", "left");
    samples.put("I MASK pizza for dinner", "ordered");
    samples.put("MASK is knocking at the door", "who");
    samples.put("the MASK is flying high", "kite");
    samples.put("he MASK the report by noon", "submitted");
    samples.put("the MASK makes me laugh", "joke");
    samples.put("it MASK very interesting", "sounds");
    samples.put("can you MASK this word", "define");
    samples.put("she MASK across the street", "ran");
    samples.put("I MASK the question correctly", "answered");
    samples.put("this MASK feels so soft", "blanket");
    samples.put("we MASK a new language", "learned");
    samples.put("MASK you see the news today", "did");
    samples.put("he MASK the glass on the table", "placed");
    samples.put("I MASK about the future often", "think");
    samples.put("MASK the door when you leave", "close");
    samples.put("she MASK on the project all night", "worked");
    samples.put("we MASK the mountain yesterday", "climbed");
    samples.put("the MASK was barking loudly", "dog");
    samples.put("I MASK him to the party", "invited");
    samples.put("MASK is your favorite sport", "what");
    samples.put("he MASK a beautiful song", "sang");
    samples.put("the MASK is very bright", "moon");
    samples.put("she MASK the book in her bag", "put");
    samples.put("I MASK the guitar every evening", "play");
    samples.put("MASK is the weather today", "how");
    samples.put("they MASK the new rules clearly", "explained");
    samples.put("we MASK to the park yesterday", "went");
    samples.put("the MASK is ringing", "phone");
    samples.put("I MASK the recipe for dinner", "followed");
    samples.put("MASK are the instructions clear", "why");
    samples.put("he MASK the painting carefully", "admired");
    samples.put("the MASK is moving very fast", "car");
    samples.put("she MASK the phone call immediately", "answered");
    samples.put("we MASK a picnic in the park", "had");
    samples.put("MASK are the guests arriving", "when");
    samples.put("he MASK on the chair comfortably", "sat");
    samples.put("I MASK my coffee black", "prefer");

    Model model = new Model(
      tokenizer,
      samples,
      new Kwargs(
        "n_vocab", 32
      ));

    model.construct()
      .forward(samples);

    double temperature = 1.0;
    samples.forEach((k, v) -> System.out.println("Sample: " + k + " -> " + model.generate(k, temperature)));
  }
}
