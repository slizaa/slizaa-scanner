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
package org.slizaa.scanner.importer.internal.parser;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slizaa.scanner.importer.IModelImporter;
import org.slizaa.scanner.importer.internal.ZipFileCache;
import org.slizaa.scanner.importer.spi.content.AnalyzeMode;
import org.slizaa.scanner.importer.spi.content.IContentDefinition;
import org.slizaa.scanner.importer.spi.content.IContentDefinitions;
import org.slizaa.scanner.importer.spi.content.IResource;
import org.slizaa.scanner.importer.spi.content.IResourceIdentifier;
import org.slizaa.scanner.importer.spi.parser.IParser;
import org.slizaa.scanner.importer.spi.parser.IParserFactory;
import org.slizaa.scanner.importer.spi.parser.IProblem;
import org.slizaa.scanner.model.IModifiableNode;

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
  static final int                                     THREAD_COUNT = Runtime.getRuntime().availableProcessors();

  /** - */
  private final Logger                                 logger       = LoggerFactory.getLogger(ModelImporter.class);

  /** - */
  private IContentDefinitions                            _systemDefinition;

  /** - */
  private File                                         _directory;

  /** - */
  private IParserFactory[]                             _parserFactories;

  private Map<IResourceIdentifier, StoredResourceNode> _storedResourcesMap;

  private Map<String, IModifiableNode>                 _storedModulesMap;

  private List<IProblem>                               _result;

  private ExecutorService                              _executorService;

  /**
   * <p>
   * Creates a new instance of type {@link ModelImporter}.
   * </p>
   */
  public ModelImporter(IContentDefinitions systemDefinition, File directory, IParserFactory... parserFactories) {

    checkNotNull(systemDefinition);
    checkNotNull(directory);
    checkNotNull(parserFactories);

    // set the project
    _systemDefinition = systemDefinition;
    _directory = directory;
    _parserFactories = parserFactories;
  }

  /**
   * {@inheritDoc}
   */
  public final IContentDefinitions getSystemDefinition() {
    return _systemDefinition;
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
      // Step 1: Read from underlying database
      //
      monitor.subTask("Reading from database...");
      readFromDatabase(progressMonitor.newChild(33));
      logger.debug("Finished reading from database: {}", stopwatch.elapsed(TimeUnit.MILLISECONDS));

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
      postProcess(progressMonitor.newChild(33));
      logger.debug("Finished post-processing: {}", stopwatch.elapsed(TimeUnit.MILLISECONDS));

    } finally {
      progressMonitor.done();
    }

    //
    return _result;
  }

  private void postProcess(final SubMonitor progressMonitor) {
    try (GraphDatabaseServiceFacade databaseServiceFacade = new GraphDatabaseServiceFacade(
        _directory.getAbsolutePath(), _parserFactories)) {

      // delete old resources
      progressMonitor.subTask("Deleting old resources...");
      
      IProgressMonitor subMonitor = progressMonitor.newChild(10);
      subMonitor.beginTask("Deleting old resources...", _storedResourcesMap.size());
      databaseServiceFacade.deleteResourceNodes(_storedResourcesMap.values(), subMonitor);

      // delete old modules
      subMonitor = progressMonitor.newChild(3);
      subMonitor.beginTask("Deleting old modules...", _storedModulesMap.size());
      databaseServiceFacade.deleteModulesNodes(_storedModulesMap.values(), subMonitor);

      //
      progressMonitor.subTask("Post-processing model...");
      
      subMonitor = progressMonitor.newChild(20);
      subMonitor.beginTask("Post-processing model...", _storedModulesMap.size());
      databaseServiceFacade.batchParseStop(_systemDefinition, subMonitor);
    }
  }

  private void internalParse(final SubMonitor submonitor) {

    // create the sub-monitor
    try (BatchInserterFacade batchInserter = new BatchInserterFacade(getDatabaseDirectory().getAbsolutePath())) {

      //
      batchInserter.setupModuleNodes(_systemDefinition, _storedModulesMap);

      // activate the zip cache. We need this here to keep the
      // zip files open while parsing the content
      ZipFileCache.instance().activateCache();

      // iterate over all the content entries
      _executorService = Executors.newFixedThreadPool(THREAD_COUNT);

      //
      final SubMonitor progressMonitor = SubMonitor.convert(submonitor, _systemDefinition.getContentDefinitions()
          .size());

      for (IContentDefinition contentDefinition : _systemDefinition.getContentDefinitions()) {

        //
        batchInserter.clearResourceMap();

        //
        Set<IResource> newAndModifiedBinaryResources = ModelImporterHelper.computeNewAndModifiedResources(
            contentDefinition.getBinaryResources(), _storedResourcesMap);

        //
        Set<IResource> newAndModifiedSourceResources = Collections.emptySet();

        //
        if (AnalyzeMode.BINARIES_AND_SOURCES.equals(contentDefinition.getAnalyzeMode())) {
          newAndModifiedSourceResources = ModelImporterHelper.computeNewAndModifiedResources(
              contentDefinition.getSourceResources(), _storedResourcesMap);
        }

        //
        IModifiableNode moduleNode = batchInserter.getOrCreateModuleNode(contentDefinition);

        //
        _result = multiThreadedParse(contentDefinition, moduleNode, newAndModifiedBinaryResources,
            newAndModifiedSourceResources, progressMonitor.newChild(1), batchInserter);

        //
        moduleNode.clearRelationships();
      }

    } finally {
      // deactivate the zip cache.
      ZipFileCache.instance().deactivateCache();

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
  private void readFromDatabase(final SubMonitor progressMonitor) {

    //
    try (GraphDatabaseServiceFacade databaseServiceFacade = new GraphDatabaseServiceFacade(
        _directory.getAbsolutePath(), _parserFactories)) {

      progressMonitor.subTask("Preparing model...");
      databaseServiceFacade.batchParseStart(_systemDefinition, progressMonitor.newChild(10));

      progressMonitor.subTask("Reading from datastore...");
      _storedResourcesMap = databaseServiceFacade.readStoredResourceNodes(progressMonitor.newChild(20));
      _storedModulesMap = databaseServiceFacade.readStoredModulesNodes(progressMonitor.newChild(3));
    }
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
  List<IProblem> multiThreadedParse(final IContentDefinition contentEntry, final IModifiableNode moduleBean,
      Collection<IResource> binaryResources, Collection<IResource> sourceResources, IProgressMonitor progressMonitor,
      BatchInserterFacade batchInserter) {

    if (progressMonitor != null) {
      progressMonitor.beginTask("Parsing...", sourceResources.size() + binaryResources.size());
    }

    //
    List<IProblem> result = new LinkedList<IProblem>();

    try {

      LoadingCache<String, Directory> directories = CacheBuilder.newBuilder().build(
          new CacheLoader<String, Directory>() {
            public Directory load(String key) {
              return new Directory(key);
            }
          });

      //
      for (IResource resource : binaryResources) {
        directories.getUnchecked(resource.getDirectory()).addBinaryResource(resource);
      }
      for (IResource resource : sourceResources) {
        directories.getUnchecked(resource.getDirectory()).addSourceResource(resource);
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

        IParser[] parsers = new IParser[_parserFactories.length];
        for (int j = 0; j < _parserFactories.length; j++) {
          parsers[j] = _parserFactories[j].createParser(_systemDefinition);
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
