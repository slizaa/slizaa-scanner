package org.slizaa.scanner.cmdline.server;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class PluginDescriptor {

  /** - */
  private String       id;

  /** - */
  private List<String> serverExtensions;

  /** - */
  private List<String> parserFactories;

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public String getId() {
    return id;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public List<String> getParserFactories() {
    return parserFactories;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public List<String> getServerExtensions() {
    return serverExtensions;
  }

  @Override
  public String toString() {
    return "PluginDescriptor [id=" + id + ", serverExtensions=" + serverExtensions + ", parserFactories="
        + parserFactories + "]";
  }
}
