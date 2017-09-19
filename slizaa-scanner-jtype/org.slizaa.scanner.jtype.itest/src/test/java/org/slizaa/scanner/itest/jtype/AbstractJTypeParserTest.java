/*******************************************************************************
 * Copyright (c) 2011-2015 Slizaa project team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Slizaa project team - initial API and implementation
 ******************************************************************************/
package org.slizaa.scanner.itest.jtype;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.configuration.BoltConnector;
import org.neo4j.kernel.configuration.Connector.ConnectorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slizaa.scanner.importer.internal.parser.ModelImporter;
import org.slizaa.scanner.jtype.bytecode.JTypeByteCodeParserFactory;
import org.slizaa.scanner.systemdefinition.ISystemDefinition;

/**
 * <p>
 * </p>
 */
public abstract class AbstractJTypeParserTest {

  @Rule
  public TemporaryFolder                _temporaryFolder = new TemporaryFolder() {
                                                           @Override
                                                           protected void after() {
                                                             if (shouldDeleteDatabase()) {
                                                               super.after();
                                                             }
                                                           }
                                                         };

  /** - */
  protected static GraphDatabaseService _graphDb;

  /** - */
  private Transaction                   _transaction;

  /** - */
  private File                          _databaseDirectory;

  /**
   * @throws IOException
   */
  @Before
  public void before() throws IOException {

    //
    if (_graphDb == null) {

      // parse...
      _databaseDirectory = parse();

      // ... and create database
      BoltConnector bolt = new BoltConnector("0");
      _graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(_databaseDirectory)
          .setConfig(bolt.type, ConnectorType.BOLT.name()).setConfig(bolt.enabled, "true")
          .setConfig(bolt.listen_address, "localhost:7687").setConfig(bolt.encryption_level, "DISABLED")
          .newGraphDatabase();
    }

    //
    if (_transaction != null) {
      _transaction.failure();
      _transaction.close();
    }

    //
    _transaction = _graphDb.beginTx();
  }

  @After
  public void after() {

    _transaction.failure();
    _transaction.close();
    _transaction = null;

    //
    if (shutDownDatabaseAfterTest() && _graphDb != null) {
      _graphDb.shutdown();
      _graphDb = null;
    }
  }

  /**
   * @return
   */
  protected boolean shutDownDatabaseAfterTest() {
    return true;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public GraphDatabaseService getGraphDatabaseService() {
    return _graphDb;
  }

  public Label convert(org.slizaa.scanner.api.model.Label label) {
    return Label.label(label.name());
  }

  public RelationshipType convert(org.slizaa.scanner.api.model.RelationshipType relationshipType) {
    return RelationshipType.withName(relationshipType.name());
  }

  protected boolean shouldDeleteDatabase() {
    return true;
  }

  protected Result executeStatement(String statement) {
    return _graphDb.execute(checkNotNull(statement));
  }

  protected Result executeStatement(String statement, Map<String, Object> parameters) {
    return _graphDb.execute(checkNotNull(statement), parameters);
  }

  protected List<Node> getNodes(Result result) {
    assertThat(result.columns()).hasSize(1);
    return result.stream().flatMap(map -> map.values().stream()).filter(o -> o instanceof Node).map(o -> (Node) o)
        .collect(Collectors.toList());
  }

  protected Node getSingleNode(Result result) {
    List<Node> nodes = getNodes(result);
    assertThat(nodes).hasSize(1);
    return nodes.get(0);
  }

  protected List<Node> nodes(String statement) {
    return getNodes(executeStatement(statement));
  }

  protected Node singleNode(String statement) {
    return getSingleNode(executeStatement(statement));
  }

  protected List<Node> nodes(String statement, Map<String, Object> parameters) {
    return getNodes(executeStatement(statement, parameters));
  }

  protected Node singleNode(String statement, Map<String, Object> parameters) {
    return getSingleNode(executeStatement(statement, parameters));
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   * @throws IOException
   */
  protected File parse() throws IOException {

    //
    File databaseDirectory = _temporaryFolder.newFolder(this.getClass().getSimpleName());

    //
    ModelImporter executer = new ModelImporter(getSystemDefinition(), databaseDirectory,
        new JTypeByteCodeParserFactory());

    executer.parse(new DummyProgressMonitor());

    //
    return databaseDirectory;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  protected abstract ISystemDefinition getSystemDefinition();

  /**
   * <p>
   * </p>
   */
  public static class DummyProgressMonitor implements IProgressMonitor {

    //
    Logger         logger = LoggerFactory.getLogger(DummyProgressMonitor.class);

    /** - */
    private double _totalWork;

    /** - */
    private double _worked;

    @Override
    public void beginTask(String name, int totalWork) {
      logger.info("beginTask({}{})", name, totalWork);
      _totalWork = totalWork;
    }

    @Override
    public void done() {
      logger.info("done()");
    }

    @Override
    public void internalWorked(double work) {
      logger.info("internalWorked({})", work);
    }

    @Override
    public boolean isCanceled() {
      return false;
    }

    @Override
    public void setCanceled(boolean value) {
    }

    @Override
    public void setTaskName(String name) {
      logger.info("setTaskName()");
    }

    @Override
    public void subTask(String name) {
      logger.info("subTask({})", name);
    }

    @Override
    public void worked(int work) {
      // logger.info("worked({})", work);
      _worked = _worked + work;
      double r = (_worked / _totalWork) * 100;
      logger.info("{}%)", r);
    }
  }
}
