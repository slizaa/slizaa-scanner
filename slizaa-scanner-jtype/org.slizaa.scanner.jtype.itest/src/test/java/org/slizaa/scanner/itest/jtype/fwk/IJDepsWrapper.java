package org.slizaa.scanner.itest.jtype.fwk;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.slizaa.scanner.core.itestfwk.aether.AetherUtils;
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

    IJDepsWrapper jdepsWrapper = new JDepsWrapper();

    File jarFile = AetherUtils.resolve("org.mapstruct", "mapstruct", "1.2.0.CR2", null, "jar");

    Map<String, List<String>> map = jdepsWrapper.analyze(jarFile.getAbsolutePath());

    System.out.println(map);
  }
}
