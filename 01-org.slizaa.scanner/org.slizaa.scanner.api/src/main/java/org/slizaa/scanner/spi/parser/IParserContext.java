package org.slizaa.scanner.spi.parser;

import org.slizaa.scanner.api.model.IModifiableNode;

public interface IParserContext {

  boolean parseReferences();

  IModifiableNode getParentDirectoryNode();

  IModifiableNode getParentModuleNode();
}
