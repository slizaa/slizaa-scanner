package org.slizaa.scanner.core.impl.graphdbfactory;

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
   * @param databaseService
   * @param port
   * @param slizaaProject
   */
  public Neo4jGraphDb(GraphDatabaseService databaseService, int port, Object userObject) {
    _databaseService = checkNotNull(databaseService);
    _port = port;
    _userObject = userObject;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T getUserObject(Class<T> type) {

    //
    if (_userObject != null && checkNotNull(type).isAssignableFrom(_userObject.getClass())) {
      return (T) _userObject;
    }

    //
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> boolean hasUserObject(Class<T> userObject) {
    return getUserObject(userObject) != null;
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
