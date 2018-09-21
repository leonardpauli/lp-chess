package com.leonardpauli.experiments.boardgame.game.notation.tokenizer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Tokenizer {
  private InputStream stream;
  private String buffer = "";
  private int cutOffset = 0;
  private static int bufferTargetSize = 1024;

  public Tokenizer(InputStream stream) {
    this.stream = stream;
  }

  public TokenizeResult tokenize(Token token, int offset) throws IOException, TokenizerException {
    if (buffer.length() - cutOffset < Tokenizer.bufferTargetSize) readMoreToBuffer();

    Token[] innerTokens = token.getInnerTokens();
    if (innerTokens.length == 0) {
      return new TokenizeResult(); // TODO: what?
    }

    String str = buffer.substring(cutOffset + offset);
    TokenizeResult res = new TokenizeResult();

    // TODO: also implement allMatched / and-list?
    for (Token t : innerTokens) {
      TokenizeResult innerTokenRes = null;

      while (innerTokenRes == null || innerTokenRes.needsMore) {
        innerTokenRes = t.getMatchResult(str);
        if (innerTokenRes.needsMore) {
          boolean withinOwnLimit = buffer.length() - offset < innerTokenRes.maxNeededStringSize;
          if (!withinOwnLimit) throw new TokenizerException("out of own limit for buffer size");
          readMoreToBuffer();
        }
      }

      if (!innerTokenRes.ok) continue;

      TokenizeResult outerTokenRes = token.handleInnerMatch(t, innerTokenRes, str);
      if (!outerTokenRes.ok) continue;

      res = outerTokenRes;
      break;
    }

    return res;
  }

  public TokenizeResult tokenize(Token token) throws IOException, TokenizerException {
    return tokenize(token, 0);
  }

  public void increaseConsumedCount(int consumedChars) {
    cutOffset += consumedChars;
  }

  private void readMoreToBuffer() throws IOException {
    // https://stackoverflow.com/questions/309424/how-to-read-convert-an-inputstream-into-a-string-in-java
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    byte[] buffer = new byte[Tokenizer.bufferTargetSize];
    int length;
    while ((length = stream.read(buffer)) != -1) {
      result.write(buffer, 0, length);
    }
    // TODO: instead of copying old string, reuse by shifting cutOffset
    // 	initialize string to bufferTargetSize*2 so no reallocation needed
    this.buffer = this.buffer.substring(cutOffset) + result.toString(StandardCharsets.UTF_8);
  }
}

