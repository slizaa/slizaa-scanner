package org.slizaa.scanner.distribution.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import org.neo4j.graphdb.GraphDatabaseService;
import org.slizaa.scanner.api.graphdb.IGraphDb;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class Neo4jGraphDb implements IGraphDb {

  /** - */
  private Object               _userObject;

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
  public Neo4jGraphDb(Object userObject, GraphDatabaseService databaseService, int port) {
    _userObject = checkNotNull(userObject);
    _databaseService = checkNotNull(databaseService);
    _port = port;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getUserObject(Class<T> type) {

    if (checkNotNull(type).isAssignableFrom(_userObject.getClass())) {
      return (T) _userObject;
    }

    return null;
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
