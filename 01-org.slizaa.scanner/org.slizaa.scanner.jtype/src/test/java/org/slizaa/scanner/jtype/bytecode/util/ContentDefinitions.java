package org.slizaa.scanner.jtype.bytecode.util;

import java.util.ArrayList;
import java.util.List;

import org.slizaa.scanner.spi.content.IContentDefinition;
import org.slizaa.scanner.spi.content.IContentDefinitions;

public class ContentDefinitions implements IContentDefinitions {

  private List<IContentDefinition> _contentDefinitions;

  public ContentDefinitions() {
    _contentDefinitions = new ArrayList<>();
  }

  @Override
  public List<IContentDefinition> getContentDefinitions() {
    return _contentDefinitions;
  }
}
