package org.slizaa.scanner.jtype.bytecode.util;

import java.util.ArrayList;
import java.util.List;

import org.slizaa.scanner.core.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.scanner.core.spi.contentdefinition.filebased.IFileBasedContentDefinition;

public class ContentDefinitions implements IContentDefinitionProvider {

  private List<IFileBasedContentDefinition> _contentDefinitions;

  public ContentDefinitions() {
    _contentDefinitions = new ArrayList<>();
  }

  @Override
  public List<IFileBasedContentDefinition> getContentDefinitions() {
    return _contentDefinitions;
  }
}
