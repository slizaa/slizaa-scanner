package org.slizaa.scanner.api.graphdb;

import java.io.File;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IGraphDbFactory {

  /**
   * <p>
   * </p>
   *
   * @param port
   * @param storeDir
   * @param extensionLoaders
   * @return
   */
  IGraphDb createGraphDb(int port, File storeDir);

  /**
   * <p>
   * </p>
   *
   * @param port
   * @param storeDir
   * @param userObject
   * @return
   */
  <T> IGraphDb createGraphDb(int port, File storeDir, T userObject);
}
