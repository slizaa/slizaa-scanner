package org.slizaa.scanner.statementrunner;

import java.io.File;
import java.util.Arrays;
import java.util.function.Consumer;

public class FileFilterGoodies {

  public static void main(String args[]) {
    listRecursive(new File(args[0]));
  }

  /**
   * This method recursively lists all .txt and .java files in a directory
   */
  private static void listRecursive(File dir) {

    Arrays.stream(dir.listFiles(
        (f, n) -> !n.startsWith(".") && (new File(f, n).isDirectory() || n.endsWith(".cypher") || n.endsWith(".cyp"))))
        .forEach(unchecked(file -> {

          //
          if (file.isFile()) {
            StatementCommentParser.parse(file.getAbsolutePath());
          }

          if (file.isDirectory()) {
            listRecursive(file);
          }
        }));
  }

  /**
   * This utility simply wraps a functional interface that throws a checked exception into a Java 8 Consumer
   */
  private static <T> Consumer<T> unchecked(CheckedConsumer<T> consumer) {
    return t -> {
      try {
        consumer.accept(t);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    };
  }

  @FunctionalInterface
  private interface CheckedConsumer<T> {
    void accept(T t) throws Exception;
  }
}