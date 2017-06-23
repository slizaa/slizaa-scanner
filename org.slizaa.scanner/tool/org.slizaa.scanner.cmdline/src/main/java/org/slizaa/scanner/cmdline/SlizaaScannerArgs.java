package org.slizaa.scanner.cmdline;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;

public class SlizaaScannerArgs {

  @Parameter
  private List<String> parameters = new ArrayList<>();

  @Parameter(names = { "--log", "--verbose", "-l", "-v" }, description = "Level of verbosity")
  private boolean      verbose    = false;

  @Parameter(names = "-groups", description = "Comma-separated list of group names to be run")
  private String       groups;

  @Parameter(names = "-debug", description = "Debug mode")
  private boolean      debug      = false;
}
