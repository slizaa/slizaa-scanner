package org.slizaa.scanner.importer.internal.parser;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slizaa.scanner.api.model.IModifiableNode;
import org.slizaa.scanner.spi.parser.IParserContext;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ParserContext implements IParserContext {

  /** - */
  private boolean         _parseReferences;

  /** - */
  private IModifiableNode _parentDirectory;

  /** - */
  private IModifiableNode _moduleDirectory;

  public ParserContext(IModifiableNode moduleDirectory, IModifiableNode parentDirectory, boolean parseReferences) {
    _parseReferences = parseReferences;
    _parentDirectory = checkNotNull(parentDirectory);
    _moduleDirectory = checkNotNull(moduleDirectory);
  }

  @Override
  public boolean parseReferences() {
    return _parseReferences;
  }

  @Override
  public IModifiableNode getParentDirectoryNode() {
    return _parentDirectory;
  }

  @Override
  public IModifiableNode getParentModuleNode() {
    return _moduleDirectory;
  }
}
