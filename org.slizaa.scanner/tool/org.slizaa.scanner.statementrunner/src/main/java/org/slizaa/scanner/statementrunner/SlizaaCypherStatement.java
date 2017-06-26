package org.slizaa.scanner.statementrunner;

public class SlizaaCypherStatement {

  /** - */
  private String   _name;

  /** - */
  private String[] _requirements;

  
  
  public SlizaaCypherStatement(String name, String[] requirements) {
    _name = name;
    _requirements = requirements;
  }

  public String getName() {
    return _name;
  }

  public String[] getRequirements() {
    return _requirements;
  }
}
