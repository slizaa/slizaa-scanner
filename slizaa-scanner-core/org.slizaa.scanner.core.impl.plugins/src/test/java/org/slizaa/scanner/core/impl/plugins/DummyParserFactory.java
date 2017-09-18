package org.slizaa.scanner.core.impl.plugins;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.scanner.spi.content.IContentDefinition;
import org.slizaa.scanner.spi.content.IContentDefinitions;
import org.slizaa.scanner.spi.parser.IParser;
import org.slizaa.scanner.spi.parser.IParserFactory;

public class DummyParserFactory implements IParserFactory {

  @Override
  public void initialize() {

  }

  @Override
  public void dispose() {

  }

  @Override
  public IParser createParser(IContentDefinitions systemDefinition) {
    return null;
  }

  @Override
  public void batchParseStart(IContentDefinitions systemDefinition, Object graphDatabase, IProgressMonitor subMonitor)
      throws Exception {
  }

  @Override
  public void batchParseStop(IContentDefinitions systemDefinition, Object graphDatabase, IProgressMonitor subMonitor)
      throws Exception {
  }

  @Override
  public void batchParseStartContentDefinition(IContentDefinition contentDefinition) throws Exception {
  }

  @Override
  public void batchParseStopContentDefinition(IContentDefinition contentDefinition) throws Exception {
  }

  @Override
  public void beforeDeleteResourceNode(Object node) {
  }
}
