/*******************************************************************************
 * Copyright (C) 2017 Gerd Wuetherich
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.slizaa.scanner.neo4j.testfwk;

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
import org.slizaa.scanner.core.api.cypherregistry.ICypherStatement;
import org.slizaa.scanner.core.api.graphdb.IGraphDb;
import org.slizaa.scanner.core.classpathscanner.IClasspathScannerFactory;
import org.slizaa.scanner.core.classpathscanner.internal.ClasspathScannerFactory;
import org.slizaa.scanner.core.cypherregistry.DefaultCypherStatement;
import org.slizaa.scanner.core.cypherregistry.SlizaaCypherFileParser;
import org.slizaa.scanner.core.spi.annotations.ParserFactory;
import org.slizaa.scanner.core.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.scanner.core.spi.parser.IParserFactory;
import org.slizaa.scanner.neo4j.graphdbfactory.GraphDbFactory;
import org.slizaa.scanner.neo4j.importer.internal.parser.ModelImporter;
import org.slizaa.scanner.neo4j.testfwk.internal.ZipUtil;

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
  private IGraphDb                             _graphDb;

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
    this(createDatabaseDirectory(), () -> contentDefinitions);
  }

  /**
   * <p>
   * Creates a new instance of type {@link SlizaaTestServerRule}.
   * </p>
   *
   * @param contentDefinitions
   */
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
    this._databaseDirectory = checkNotNull(workingDirectory);
    this._contentDefinitionsSupplier = checkNotNull(contentDefinitions);
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public File getDatabaseDirectory() {
    return this._databaseDirectory;
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
        List<ICypherStatement> cypherStatements = new ArrayList<>();

        scannerFactory.createScanner(this.getClass().getClassLoader())
            .matchClassesWithAnnotation(ParserFactory.class, (source, classes) -> {
              classes.forEach(c -> {
                try {
                  parserFactories.add((IParserFactory) c.newInstance());
                } catch (Exception e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                }
              });
            })
            //
            .matchFiles("cypher",

                // parse the statements
                (relativePath, inputStream, lengthBytes) -> {
                  DefaultCypherStatement statement = SlizaaCypherFileParser.parse(relativePath, inputStream);
                  statement.setRelativePath(relativePath);
                  return statement;
                },

                // fill the collector
                (codeSource, statementList) -> {
                  for (ICypherStatement cypherStatement : statementList) {
                    if (cypherStatement.isValid()) {
                      ((DefaultCypherStatement) cypherStatement).setCodeSource(codeSource);
                      cypherStatements.add(cypherStatement);
                    }
                  }
                })
            .scan();

        // parse
        ModelImporter executer = new ModelImporter(SlizaaTestServerRule.this._contentDefinitionsSupplier.get(),
            SlizaaTestServerRule.this._databaseDirectory, parserFactories, cypherStatements);

        executer.parse(new SlizaaTestProgressMonitor(),
            () -> new GraphDbFactory().newGraphDb(5001, SlizaaTestServerRule.this._databaseDirectory).create());

        //
        SlizaaTestServerRule.this._graphDb = executer.getGraphDb();

        try {
          base.evaluate();
        } finally {
          try {
            SlizaaTestServerRule.this._graphDb.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    };
  }

  /**
   * <p>
   * </p>
   *
   * @param file
   * @throws Exception
   */
  public void exportDatabaseAsZipFile(String file, boolean restart) throws Exception {
    this._graphDb.shutdown();

    ZipUtil.zipFile(this._databaseDirectory.getAbsolutePath(), checkNotNull(file), true);
    if (restart) {
      this._graphDb = new GraphDbFactory().newGraphDb(5001, this._databaseDirectory).create();
    }
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
      this.logger.info("beginTask({}{})", name, totalWork);
      this._totalWork = totalWork;
    }

    @Override
    public void done() {
      this.logger.info("done()");
    }

    @Override
    public void internalWorked(double work) {
      this.logger.info("internalWorked({})", work);
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
      this.logger.info("setTaskName()");
    }

    @Override
    public void subTask(String name) {
      this.logger.info("subTask({})", name);
    }

    @Override
    public void worked(int work) {
      // logger.info("worked({})", work);
      this._worked = this._worked + work;
      double r = (this._worked / this._totalWork) * 100;
      this.logger.info("{}%)", r);
    }
  }
}
