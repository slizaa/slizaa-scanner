package org.slizaa.scanner.core.impl.cmdline;

import java.io.File;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import com.beust.jcommander.converters.IntegerConverter;

public class SlizaaScannerArgs {

  @Parameter(description = "command", required = true)
  private String command;

  @Parameter(names = { "--log", "--verbose", "-l", "-v" }, description = "Level of verbosity")
  private boolean       verbose = false;

  @Parameter(names = { "--directory", "-d" }, converter = FileConverter.class, required = true)
  private File          directory;

  @Parameter(names = { "--port", "-p" }, converter = IntegerConverter.class)
  private int           port    = 5001;

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public boolean isVerbose() {
    return verbose;
  }

  public String getCommand() {
    return command;
  }

  public File getDirectory() {
    return directory;
  }

  public int getPort() {
    return port;
  }
}
