package org.slizaa.scanner.eclipse.itest;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import org.junit.Test;
import org.osgi.framework.BundleException;
import org.slizaa.scanner.core.api.importer.IModelImporterFactory;

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
  }
}