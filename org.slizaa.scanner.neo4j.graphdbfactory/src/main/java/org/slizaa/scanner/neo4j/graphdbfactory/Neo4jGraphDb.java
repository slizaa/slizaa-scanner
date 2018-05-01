/*******************************************************************************
 * Copyright (C) 2017 Gerd Wuetherich
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.slizaa.scanner.neo4j.graphdbfactory;

import static com.google.common.base.Preconditions.checkNotNull;

import org.neo4j.graphdb.GraphDatabaseService;
import org.slizaa.scanner.core.api.graphdb.IGraphDb;

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

  @Override
  public void close() throws Exception {
    _databaseService.shutdown();
  }
}
