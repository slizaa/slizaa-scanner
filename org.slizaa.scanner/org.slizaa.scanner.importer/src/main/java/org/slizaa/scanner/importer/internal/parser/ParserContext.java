package org.slizaa.scanner.importer.internal.parser;

import org.slizaa.scanner.spi.parser.IParserContext;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ParserContext implements IParserContext {

  /** - */
  private boolean _parseReferences;

  public ParserContext(boolean parseReferences) {
    _parseReferences = parseReferences;
  }

  @Override
  public boolean parseReferences() {
    return _parseReferences;
  }
}
