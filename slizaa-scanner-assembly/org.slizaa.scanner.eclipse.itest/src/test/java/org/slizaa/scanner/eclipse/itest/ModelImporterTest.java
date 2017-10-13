package org.slizaa.scanner.eclipse.itest;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.Test;
import org.osgi.framework.BundleException;
import org.slizaa.scanner.core.api.importer.IModelImporterFactory;
import org.slizaa.scanner.jtype.bytecode.JTypeByteCodeParserFactory;

public class ModelImporterTest extends AbstractEclipseTest {

  /** - */
  @Inject
  private IModelImporterFactory _modelImporterFactory;

  /**
   * <p>
   * </p>
   * 
   * @throws BundleException
   */
  @Test
  public void checkParserFactories() throws BundleException {

    //
    assertThat(_modelImporterFactory.getAllParserFactories()).hasSize(1);
    assertThat(_modelImporterFactory.getAllParserFactories()[0] instanceof JTypeByteCodeParserFactory).isTrue();
  }
}