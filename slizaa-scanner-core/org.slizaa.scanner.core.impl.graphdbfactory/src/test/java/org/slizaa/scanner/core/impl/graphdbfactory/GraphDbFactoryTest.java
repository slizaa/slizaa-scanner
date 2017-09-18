package org.slizaa.scanner.core.impl.graphdbfactory;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slizaa.scanner.api.graphdb.IGraphDb;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class GraphDbFactoryTest {

  /** - */
  @Rule
  public TemporaryFolder _temporaryFolder = new TemporaryFolder();

  /**
   * <p>
   * </p>
   */
  @Test
  public void testGraphDbFactory() {

    //
    GraphDbFactory graphDbFactory = new GraphDbFactory(() -> Collections.emptyList());
    IGraphDb graphDb = graphDbFactory.createGraphDb(5001, _temporaryFolder.getRoot(), null);

    //
    assertThat(graphDb).isNotNull();
  }
}
