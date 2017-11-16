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
package org.slizaa.scanner.neo4j.importer.internal.parser;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slizaa.scanner.core.api.cypherregistry.ICypherStatement;
import org.slizaa.scanner.core.api.importer.IModelImporter;
import org.slizaa.scanner.core.spi.contentdefinition.AnalyzeMode;
import org.slizaa.scanner.core.spi.contentdefinition.IContentDefinition;
import org.slizaa.scanner.core.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.scanner.core.spi.contentdefinition.filebased.IFile;
import org.slizaa.scanner.core.spi.contentdefinition.filebased.IFileBasedContentDefinition;
import org.slizaa.scanner.core.spi.parser.IParser;
import org.slizaa.scanner.core.spi.parser.IParserFactory;
import org.slizaa.scanner.core.spi.parser.IProblem;
import org.slizaa.scanner.core.spi.parser.model.INode;

import com.google.common.base.Stopwatch;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ModelImporter implements IModelImporter {

  /** THREAD_COUNT */
  static final int                   THREAD_COUNT = Runtime.getRuntime().availableProcessors();

  /** - */
  private final Logger               logger       = LoggerFactory.getLogger(ModelImporter.class);

  /** - */
  private IContentDefinitionProvider _contentDefinitions;

  /** - */
  private File                       _directory;

  /** - */
  private List<IParserFactory>       _parserFactories;

  /** - */
  private List<ICypherStatement>     _cypherStatements;

  /** - */
  private List<IProblem>             _result;

  /** - */
  private ExecutorService            _executorService;

  /**
   * <p>
   * Creates a new instance of type {@link ModelImporter}.
   * </p>
   */
  public ModelImporter(IContentDefinitionProvider systemDefinition, File directory,
      List<IParserFactory> parserFactories, List<ICypherStatement> cypherStatements) {

    checkNotNull(systemDefinition);
    checkNotNull(directory);
    checkNotNull(parserFactories);

    // set the project
    _contentDefinitions = systemDefinition;
    _directory = directory;
    _parserFactories = parserFactories;
  }

  /**
   * {@inheritDoc}
   */
  public final IContentDefinitionProvider getSystemDefinition() {
    return _contentDefinitions;
  }

  /**
   * {@inheritDoc}
   */
  public File getDatabaseDirectory() {
    return _directory;
  }

  /**
   * <p>
   * </p>
   * 
   * @param monitor
   * @return
   */
  @Override
  public List<IProblem> parse(IProgressMonitor monitor) {

    // create new null monitor if necessary
    if (monitor == null) {
      monitor = new NullProgressMonitor();
    }

    // create the sub-monitor
    final SubMonitor progressMonitor = SubMonitor.convert(monitor, 100);

    _result = Collections.emptyList();

    Stopwatch stopwatch = Stopwatch.createStarted();

    try {

      //
      // Step 1: Pre-Processing
      //
      monitor.subTask("Pre-Processing...");
      startBatchParse(progressMonitor.newChild(33));
      logger.debug("Finished pre-processing: {}", stopwatch.elapsed(TimeUnit.MILLISECONDS));

      //
      // Step 2: Parse elements
      //
      monitor.subTask("Parsing...");
      internalParse(progressMonitor.newChild(34));
      logger.debug("Finished parsing and inserting: {}", stopwatch.elapsed(TimeUnit.MILLISECONDS));

      //
      // Step 3: Post-Processing
      //
      monitor.subTask("Post-Processing...");
      stopBatchParse(progressMonitor.newChild(33));
      logger.debug("Finished post-processing: {}", stopwatch.elapsed(TimeUnit.MILLISECONDS));

    } finally {
      progressMonitor.done();
    }

    //
    return _result;
  }

  private void internalParse(final SubMonitor submonitor) {

    // create the sub-monitor
    try (BatchInserterFacade batchInserter = new BatchInserterFacade(getDatabaseDirectory().getAbsolutePath())) {

      // iterate over all the content entries
      _executorService = Executors.newFixedThreadPool(THREAD_COUNT);

      //
      final SubMonitor progressMonitor = SubMonitor.convert(submonitor,
          _contentDefinitions.getContentDefinitions().size());

      for (IContentDefinition definition : _contentDefinitions.getContentDefinitions()) {

        //
        if (definition instanceof IFileBasedContentDefinition) {

          //
          IFileBasedContentDefinition fileBasedContentDefinition = (IFileBasedContentDefinition) definition;

          //
          for (IParserFactory parserFactory : _parserFactories) {
            try {
              parserFactory.batchParseStartContentDefinition(fileBasedContentDefinition);
            } catch (Exception e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }

          //
          batchInserter.clearResourceAndDirectoriesMap();

          //
          INode moduleNode = batchInserter.getOrCreateModuleNode(fileBasedContentDefinition);

          //
          _result = multiThreadedParse(fileBasedContentDefinition, moduleNode,
              fileBasedContentDefinition.getBinaryFiles(),
              AnalyzeMode.BINARIES_AND_SOURCES.equals(fileBasedContentDefinition.getAnalyzeMode())
                  ? fileBasedContentDefinition.getSourceFiles()
                  : Collections.emptySet(),
              progressMonitor.newChild(1), batchInserter);

          //
          moduleNode.clearRelationships();

          //
          for (IParserFactory parserFactory : _parserFactories) {
            try {
              parserFactory.batchParseStopContentDefinition(fileBasedContentDefinition);
            } catch (Exception e) {
              //
              e.printStackTrace();
            }
          }
        }

      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    finally {

      //
      logger.debug("Save to disk...");
      _executorService.shutdown();
      _executorService = null;

      //
      if (submonitor != null) {
        submonitor.done();
      }
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param progressMonitor
   */
  private void startBatchParse(final SubMonitor progressMonitor) {

    //
    GraphDatabaseService graphDatabaseService = new GraphDatabaseFactory()
        .newEmbeddedDatabaseBuilder(getDatabaseDirectory()).newGraphDatabase();

    // create the sub-monitor
    final SubMonitor subMonitor = SubMonitor.convert(progressMonitor, _parserFactories.size());

    //
    for (IParserFactory parserFactory : _parserFactories) {

      //
      try {
        parserFactory.batchParseStart(_contentDefinitions, graphDatabaseService, subMonitor);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    //
    graphDatabaseService.shutdown();
  }

  /**
   * <p>
   * </p>
   *
   * @param progressMonitor
   */
  private void stopBatchParse(SubMonitor progressMonitor) {

    //
    GraphDatabaseService graphDatabaseService = new GraphDatabaseFactory()
        .newEmbeddedDatabaseBuilder(getDatabaseDirectory()).newGraphDatabase();

    // create the sub-monitor
    final SubMonitor subMonitor = SubMonitor.convert(progressMonitor, _parserFactories.size());

    //
    for (IParserFactory parserFactory : _parserFactories) {

      //
      try {
        parserFactory.batchParseStop(_contentDefinitions, graphDatabaseService, subMonitor);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    //
    graphDatabaseService.shutdown();
  }

  /**
   * <p>
   * </p>
   * 
   * @param contentEntry
   * @param binaryResources
   * @param sourceResources
   * @return
   */
  @SuppressWarnings("unchecked")
  List<IProblem> multiThreadedParse(final IContentDefinition contentEntry, final INode moduleBean,
      Collection<IFile> binaryResources, Collection<IFile> sourceResources, IProgressMonitor progressMonitor,
      BatchInserterFacade batchInserter) {

    if (progressMonitor != null) {
      progressMonitor.beginTask("Parsing...", sourceResources.size() + binaryResources.size());
    }

    //
    List<IProblem> result = new LinkedList<IProblem>();

    try {

      LoadingCache<String, Directory> directories = CacheBuilder.newBuilder()
          .build(new CacheLoader<String, Directory>() {
            public Directory load(String key) {
              return new Directory(key);
            }
          });

      //
      for (IFile resource : binaryResources) {
        directories.getUnchecked(resource.getDirectory()).addBinaryResource(resource);
      }
      for (IFile resource : sourceResources) {
        directories.getUnchecked(resource.getDirectory()).addSourceResource(resource);
      }

      // create directory nodes (still single-threaded!)
      for (final Directory directory : directories.asMap().values()) {
        batchInserter.getOrCreateDirectoyNode(directory, moduleBean);
      }

      // compute the part size
      float partSizeAsFloat = directories.size() / (float) THREAD_COUNT;
      int partSize = (int) Math.ceil(partSizeAsFloat);

      // split the package list in n sublist (one for each thread)
      List<Directory> dirs = new ArrayList<Directory>(directories.asMap().values());
      List<Directory>[] packageFragmentsParts = new List[THREAD_COUNT];
      for (int i = 0; i < THREAD_COUNT; i++) {
        if ((i + 1) * partSize <= directories.size()) {
          packageFragmentsParts[i] = dirs.subList(i * partSize, (i + 1) * partSize);
        } else if ((i) * partSize <= dirs.size()) {
          packageFragmentsParts[i] = dirs.subList(i * partSize, dirs.size());
        } else {
          packageFragmentsParts[i] = Collections.emptyList();
        }
      }

      // set up the callables
      ParseJob[] jobs = new ParseJob[THREAD_COUNT];
      for (int i = 0; i < jobs.length; i++) {

        IParser[] parsers = new IParser[_parserFactories.size()];
        for (int j = 0; j < _parserFactories.size(); j++) {
          parsers[j] = _parserFactories.get(j).createParser(_contentDefinitions);
        }

        jobs[i] = new ParseJob(contentEntry, moduleBean, packageFragmentsParts[i], parsers, batchInserter,
            progressMonitor);
      }

      // create the future tasks
      FutureTask<List<IProblem>>[] futureTasks = new FutureTask[THREAD_COUNT];
      for (int i = 0; i < futureTasks.length; i++) {
        futureTasks[i] = new FutureTask<List<IProblem>>(jobs[i]);
        _executorService.execute(futureTasks[i]);
      }

      // collect the result
      for (int i = 0; i < futureTasks.length; i++) {
        try {
          result.addAll(futureTasks[i].get());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

    } finally {
      batchInserter.create(moduleBean);

      if (progressMonitor != null) {
        progressMonitor.done();
      }
    }

    //
    return result;
  }
}
