package org.slizaa.scanner.distribution.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import org.neo4j.graphdb.GraphDatabaseService;
import org.slizaa.product.project.ISlizaaProject;
import org.slizaa.product.project.spi.IGraphDb;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class Neo4jGraphDb implements IGraphDb {

  /** - */
  private ISlizaaProject       _slizaaProject;

  /** - */
  private GraphDatabaseService _databaseService;

  /** - */
  private int                  _port;

  /**
   * <p>
   * Creates a new instance of type {@link Neo4jGraphDb}.
   * </p>
   *
   * @param slizaaProject
   * @param databaseService
   * @param port
   */
  public Neo4jGraphDb(ISlizaaProject slizaaProject, GraphDatabaseService databaseService, int port) {
    _slizaaProject = checkNotNull(slizaaProject);
    _databaseService = checkNotNull(databaseService);
    _port = port;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ISlizaaProject getSlizaaProject() {
    return _slizaaProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getPort() {
    return _port;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void shutdown() {
    _databaseService.shutdown();
  }
}
