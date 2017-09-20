package org.neo4j.harness.junit;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.core.runtime.IProgressMonitor;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.config.Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slizaa.scanner.importer.internal.parser.ModelImporter;
import org.slizaa.scanner.itest.jtype.AbstractJTypeParserTest.DummyProgressMonitor;
import org.slizaa.scanner.spi.content.IContentDefinition;
import org.slizaa.scanner.spi.content.IContentDefinitions;
import org.slizaa.scanner.spi.parser.IParserFactory;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SlizaaTestServerRule extends Neo4jRule {

  /** - */
  private File                                  _databaseDirectory;

  /** - */
  private IContentDefinitions                   _contentDefinitions;

  /** - */
  private List<Class<? extends IParserFactory>> _parserFactories;

  /**
   * <p>
   * Creates a new instance of type {@link SlizaaTestServerRule}.
   * </p>
   *
   * @throws IOException
   */
  public SlizaaTestServerRule() {
    this(createDatabaseDirectory());
  }

  /**
   * <p>
   * Creates a new instance of type {@link SlizaaTestServerRule}.
   * </p>
   *
   * @param workingDirectory
   */
  private SlizaaTestServerRule(File workingDirectory) {
    super(new SlizaaProcessServerBuilder(workingDirectory));

    _databaseDirectory = workingDirectory;
    _parserFactories = new LinkedList<>();
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public File getDatabaseDirectory() {
    return _databaseDirectory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Statement apply(Statement base, Description description) {

    //
    if (_contentDefinitions == null) {
      _contentDefinitions = new IContentDefinitions() {
        @Override
        public List<IContentDefinition> getContentDefinitions() {
          return Collections.emptyList();
        }
      };
    }

    //
    List<IParserFactory> parserFactories = new LinkedList<>();
    for (Class<? extends IParserFactory> clazz : _parserFactories) {
      try {
        parserFactories.add(clazz.newInstance());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    // parse
    ModelImporter executer = new ModelImporter(_contentDefinitions, new File(_databaseDirectory, "databases/graph.db"),
        parserFactories.toArray(new IParserFactory[0]));
    executer.parse(new SlizaaTestProgressMonitor());

    //
    return super.apply(base, description);
  }

  public SlizaaTestServerRule withContentDefinitions(IContentDefinitions contentDefinitions) {
    _contentDefinitions = checkNotNull(contentDefinitions);
    return this;
  }

  public SlizaaTestServerRule withParserFactory(Class<? extends IParserFactory> parserFactoryClass) {
    _parserFactories.add(checkNotNull(parserFactoryClass));
    return this;
  }

  @Override
  public SlizaaTestServerRule withConfig(Setting<?> key, String value) {
    return (SlizaaTestServerRule) super.withConfig(key, value);
  }

  @Override
  public SlizaaTestServerRule withConfig(String key, String value) {
    return (SlizaaTestServerRule) super.withConfig(key, value);
  }

  @Override
  public SlizaaTestServerRule withExtension(String mountPath, Class<?> extension) {
    return (SlizaaTestServerRule) super.withExtension(mountPath, extension);
  }

  @Override
  public SlizaaTestServerRule withExtension(String mountPath, String packageName) {
    return (SlizaaTestServerRule) super.withExtension(mountPath, packageName);
  }

  @Override
  public SlizaaTestServerRule withFixture(File cypherFileOrDirectory) {
    return (SlizaaTestServerRule) super.withFixture(cypherFileOrDirectory);
  }

  @Override
  public SlizaaTestServerRule withFixture(String fixtureStatement) {
    return (SlizaaTestServerRule) super.withFixture(fixtureStatement);
  }

  @Override
  public SlizaaTestServerRule withFixture(Function<GraphDatabaseService, Void> fixtureFunction) {
    return (SlizaaTestServerRule) super.withFixture(fixtureFunction);
  }

  @Override
  public SlizaaTestServerRule withProcedure(Class<?> procedureClass) {
    return (SlizaaTestServerRule) super.withProcedure(procedureClass);
  }

  @Override
  public SlizaaTestServerRule withFunction(Class<?> functionClass) {
    return (SlizaaTestServerRule) super.withFunction(functionClass);
  }

  @Override
  public SlizaaTestServerRule withAggregationFunction(Class<?> functionClass) {
    return (SlizaaTestServerRule) super.withAggregationFunction(functionClass);
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  private static File createDatabaseDirectory() {
    try {
      return Files.createTempDirectory("slizaaTestDatabases").toFile();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * <p>
   * </p>
   */
  public static class SlizaaTestProgressMonitor implements IProgressMonitor {

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
