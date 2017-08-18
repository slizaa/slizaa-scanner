package org.slizaa.scanner.itest.framework.jdeps;

import java.util.List;
import java.util.Map;

import org.slizaa.scanner.itest.framework.jdeps.internal.JDepsWrapper;

public class Main {

  /**
   * <p>
   * </p>
   *
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {

    IJDepsWrapper jdepsWrapper = new JDepsWrapper();

    Map<String, List<String>> map = jdepsWrapper.analyze(
        "C:\\Users\\wuetherich\\.m2\\repository\\org\\slizaa\\org.slizaa.neo4j.dbadapter\\0.0.4-SNAPSHOT\\org.slizaa.neo4j.dbadapter-0.0.4-SNAPSHOT.jar");

    System.out.println(map);
  }
}
