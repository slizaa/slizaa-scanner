package org.slizaa.scanner.jtype.bytecode.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.slizaa.scanner.core.spi.contentdefinition.AnalyzeMode;
import org.slizaa.scanner.core.spi.contentdefinition.ContentType;
import org.slizaa.scanner.core.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.scanner.core.spi.contentdefinition.filebased.IFile;
import org.slizaa.scanner.core.spi.contentdefinition.filebased.IFileBasedContentDefinition;

public class ContentDefinition implements IFileBasedContentDefinition {

  /** - */
  private Collection<IFile>          _binaryResources;

  /** - */
  private IContentDefinitionProvider _contentDefinitions;

  /**
   * <p>
   * Creates a new instance of type {@link ContentDefinition}.
   * </p>
   */
  public ContentDefinition(IContentDefinitionProvider contentDefinitions) {
    _contentDefinitions = checkNotNull(contentDefinitions);
    _binaryResources = new ArrayList<>();
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
  public Collection<IFile> getFiles(ContentType type) {

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
  public Collection<IFile> getBinaryFiles() {
    return getFiles(ContentType.BINARY);
  }

  @Override
  public Collection<IFile> getSourceFiles() {
    return getFiles(ContentType.SOURCE);
  }
}
