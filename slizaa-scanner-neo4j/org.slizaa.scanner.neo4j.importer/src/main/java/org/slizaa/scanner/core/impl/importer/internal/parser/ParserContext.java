package org.slizaa.scanner.core.impl.importer.internal.parser;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slizaa.scanner.core.spi.parser.IParserContext;
import org.slizaa.scanner.core.spi.parser.model.INode;

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
  private INode _parentDirectory;

  /** - */
  private INode _moduleDirectory;

  public ParserContext(INode moduleDirectory, INode parentDirectory, boolean parseReferences) {
    _parseReferences = parseReferences;
    _parentDirectory = checkNotNull(parentDirectory);
    _moduleDirectory = checkNotNull(moduleDirectory);
  }

  @Override
  public boolean parseReferences() {
    return _parseReferences;
  }

  @Override
  public INode getParentDirectoryNode() {
    return _parentDirectory;
  }

  @Override
  public INode getParentModuleNode() {
    return _moduleDirectory;
  }
}
