package org.slizaa.scanner.neo4j.testfwk.junit;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.eclipse.core.runtime.IProgressMonitor;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slizaa.scanner.core.api.graphdb.IGraphDb;
import org.slizaa.scanner.core.classpathscanner.IClasspathScannerFactory;
import org.slizaa.scanner.core.classpathscanner.internal.ClasspathScannerFactory;
import org.slizaa.scanner.core.spi.annotations.SlizaaParserFactory;
import org.slizaa.scanner.core.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.scanner.core.spi.parser.IParserFactory;
import org.slizaa.scanner.neo4j.graphdbfactory.GraphDbFactory;
import org.slizaa.scanner.neo4j.importer.internal.parser.ModelImporter;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SlizaaTestServerRule implements TestRule {

  /** - */
  private File                                 _databaseDirectory;

  /** - */
  private Supplier<IContentDefinitionProvider> _contentDefinitionsSupplier;

  /**
   * <p>
   * Creates a new instance of type {@link SlizaaTestServerRule}.
   * </p>
   *
   * @param contentDefinitions
   */
  public SlizaaTestServerRule(IContentDefinitionProvider contentDefinitions) {
    this(createDatabaseDirectory(), new Supplier<IContentDefinitionProvider>() {
      @Override
      public IContentDefinitionProvider get() {
        return contentDefinitions;
      }
    });
  }

  public SlizaaTestServerRule(Supplier<IContentDefinitionProvider> contentDefinitions) {
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
  private SlizaaTestServerRule(File workingDirectory, Supplier<IContentDefinitionProvider> contentDefinitions) {
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
        IClasspathScannerFactory scannerFactory = new ClasspathScannerFactory()
            .registerCodeSourceClassLoaderProvider(ClassLoader.class, cl -> cl);

        //
        List<IParserFactory> parserFactories = new ArrayList<>();
        scannerFactory.createScanner(this.getClass().getClassLoader())
            .matchClassesWithAnnotation(SlizaaParserFactory.class, (source, classes) -> {
              classes.forEach(c -> {
                try {
                  parserFactories.add((IParserFactory) c.newInstance());
                } catch (Exception e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                }
              });
            }).scan();

        // parse
        ModelImporter executer = new ModelImporter(_contentDefinitionsSupplier.get(), _databaseDirectory,
            parserFactories.toArray(new IParserFactory[0]));

        executer.parse(new SlizaaTestProgressMonitor());

        //
        GraphDbFactory graphDbFactory = new GraphDbFactory();

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
