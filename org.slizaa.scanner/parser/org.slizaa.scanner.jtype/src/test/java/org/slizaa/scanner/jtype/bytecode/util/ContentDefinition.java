package org.slizaa.scanner.jtype.bytecode.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.slizaa.scanner.spi.content.AnalyzeMode;
import org.slizaa.scanner.spi.content.IContentDefinition;
import org.slizaa.scanner.spi.content.IResource;
import org.slizaa.scanner.spi.content.ResourceType;

public class ContentDefinition implements IContentDefinition {

  /** - */
  private Collection<IResource> _binaryResources;

  /**
   * <p>
   * Creates a new instance of type {@link ContentDefinition}.
   * </p>
   */
  public ContentDefinition() {
    _binaryResources = new ArrayList<>();
  }

  @Override
  public String getId() {
    return "00001";
  }

  @Override
  public String getName() {
    return "Test";
  }

  @Override
  public String getVersion() {
    return "1.0.0";
  }

  @Override
  public boolean isAnalyze() {
    return true;
  }

  @Override
  public AnalyzeMode getAnalyzeMode() {
    return AnalyzeMode.BINARIES_ONLY;
  }

  @Override
  public Collection<IResource> getResources(ResourceType type) {

    //
    switch (type) {
    case BINARY:
      return _binaryResources;
    case SOURCE:
      return Collections.emptyList();
    }

    //
    return _binaryResources;
  }

  @Override
  public Collection<IResource> getBinaryResources() {
    return getResources(ResourceType.BINARY);
  }

  @Override
  public Collection<IResource> getSourceResources() {
    return getResources(ResourceType.SOURCE);
  }
}
