package org.slizaa.scanner.core.testfwk.junit;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IProgressMonitor;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slizaa.scanner.api.graphdb.IGraphDb;
import org.slizaa.scanner.core.impl.graphdbfactory.GraphDbFactory;
import org.slizaa.scanner.core.impl.plugins.SlizaaPluginRegistry;
import org.slizaa.scanner.importer.internal.parser.ModelImporter;
import org.slizaa.scanner.spi.content.IContentDefinitions;
import org.slizaa.scanner.spi.parser.IParserFactory;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SlizaaTestServerRule implements TestRule {

  /** - */
  private File                          _databaseDirectory;

  /** - */
  private Supplier<IContentDefinitions> _contentDefinitionsSupplier;

  /**
   * <p>
   * Creates a new instance of type {@link SlizaaTestServerRule}.
   * </p>
   *
   * @param contentDefinitions
   */
  public SlizaaTestServerRule(IContentDefinitions contentDefinitions) {
    this(createDatabaseDirectory(), new Supplier<IContentDefinitions>() {
      @Override
      public IContentDefinitions get() {
        return contentDefinitions;
      }
    });
  }

  public SlizaaTestServerRule(Supplier<IContentDefinitions> contentDefinitions) {
    this(createDatabaseDirectory(), contentDefinitions);
  }

  /**
   * <p>
   * Creates a new instance of type {@link SlizaaTestServerRule}.
   * </p>
   *
   * @param workingDirectory
   * @param contentDefinitions
   */
  private SlizaaTestServerRule(File workingDirectory, Supplier<IContentDefinitions> contentDefinitions) {
    checkNotNull(contentDefinitions);
    _databaseDirectory = checkNotNull(workingDirectory);
    _contentDefinitionsSupplier = checkNotNull(contentDefinitions);
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

    return new Statement() {

      @Override
      public void evaluate() throws Throwable {

        //
        SlizaaPluginRegistry pluginRegistry = new SlizaaPluginRegistry(Arrays.asList(this.getClass().getClassLoader()));
        pluginRegistry.initialize();

        //
        List<IParserFactory> factories = new ArrayList<IParserFactory>();
        for (Class<? extends IParserFactory> parserFactoryClass : pluginRegistry.getParserFactories()) {
          factories.add(parserFactoryClass.newInstance());
        }

        // parse
        ModelImporter executer = new ModelImporter(_contentDefinitionsSupplier.get(), _databaseDirectory,
            factories.toArray(new IParserFactory[0]));

        executer.parse(new SlizaaTestProgressMonitor());

        //
        GraphDbFactory graphDbFactory = new GraphDbFactory(() -> pluginRegistry.getNeo4jExtensions());

        //
        try (IGraphDb graphDb = graphDbFactory.createGraphDb(5001, _databaseDirectory)) {
          base.evaluate();
        }
      }
    };
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
    Logger         logger = LoggerFactory.getLogger(SlizaaTestProgressMonitor.class);

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
