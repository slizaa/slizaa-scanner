package org.slizaa.scanner.statementrunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class StatementCommentParser {

  public static final void parse(String fileName) throws IOException {
    try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
      stream.forEach(line -> {
        if (isLineComment(line)) {
          System.out.println(getCommentContent(line));
        }
      });
    }
  }

  public static final boolean isLineComment(String line) {
    return line.trim().startsWith("//");
  }

  public static final String getCommentContent(String line) {
    String trimmedLine = line.trim();
    return trimmedLine.substring(2, trimmedLine.length()).trim();
  }
}
