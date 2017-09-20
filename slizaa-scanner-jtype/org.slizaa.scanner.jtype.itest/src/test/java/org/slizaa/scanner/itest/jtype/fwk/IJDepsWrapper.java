package org.slizaa.scanner.itest.jtype.fwk;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.ops4j.pax.url.mvn.MavenResolver;
import org.ops4j.pax.url.mvn.MavenResolvers;
import org.slizaa.scanner.itest.jtype.fwk.internal.JDepsWrapper;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IJDepsWrapper {

  /**
   * <p>
   * </p>
   *
   * @param jarFile
   * @return
   */
  Map<String, List<String>> analyze(String jarFile);

  /**
   * <p>
   * </p>
   *
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  public static class Factory {

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public static IJDepsWrapper create() {
      return new JDepsWrapper();
    }
  }

  /**
   * <p>
   * </p>
   *
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {

    //
    MavenResolver mavenResolver = MavenResolvers.createMavenResolver(null, null);
    File jarFile = mavenResolver.resolve("org.mapstruct", "mapstruct", null, null, "1.2.0.CR2");

    //
    IJDepsWrapper jdepsWrapper = new JDepsWrapper();

    Map<String, List<String>> map = jdepsWrapper.analyze(jarFile.getAbsolutePath());
    System.out.println(map);
  }
}
