package org.slizaa.scanner.itest.framework.jdeps;

import java.util.List;
import java.util.Map;

import org.slizaa.scanner.itest.framework.jdeps.internal.JDepsWrapper;

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
}
