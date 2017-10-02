package org.slizaa.scanner.jtype.bytecode.util;

import java.util.ArrayList;
import java.util.List;

import org.slizaa.scanner.core.spi.contentdefinition.IContentDefinition;
import org.slizaa.scanner.core.spi.contentdefinition.IContentDefinitionProvider;

public class ContentDefinitions implements IContentDefinitionProvider {

  private List<IContentDefinition> _contentDefinitions;

  public ContentDefinitions() {
    _contentDefinitions = new ArrayList<>();
  }

  @Override
  public List<IContentDefinition> getContentDefinitions() {
    return _contentDefinitions;
  }
}
