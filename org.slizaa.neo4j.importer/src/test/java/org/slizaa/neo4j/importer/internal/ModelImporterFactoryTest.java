package org.slizaa.neo4j.importer.internal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.slizaa.core.progressmonitor.DefaultProgressMonitor;
import org.slizaa.core.progressmonitor.IProgressMonitor;
import org.slizaa.scanner.api.importer.IModelImporter;
import org.slizaa.scanner.contentdefinition.MvnBasedContentDefinitionProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;

public class ModelImporterFactoryTest {

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Test
  public void testModelImporterFactory() throws IOException {

    File databaseDirectory = temporaryFolder.newFolder();

    //
    MvnBasedContentDefinitionProvider contentDefinitionProvider = new MvnBasedContentDefinitionProvider();
    contentDefinitionProvider.addArtifact("org.springframework", "spring-core", "5.0.9.RELEASE");
    contentDefinitionProvider.addArtifact("org.springframework", "spring-context", "5.0.9.RELEASE");

    // delete all contained files
    Files.walk(databaseDirectory.toPath(), FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder())
        .map(Path::toFile).forEach(File::delete);

    //
    IModelImporter modelImporter = new ModelImporterFactory().createModelImporter(
        (org.slizaa.scanner.spi.contentdefinition.IContentDefinitionProvider) contentDefinitionProvider,
        databaseDirectory, Collections.emptyList(),
        Collections.emptyList());

    IProgressMonitor progressMonitor = new DefaultProgressMonitor( "Parsing...", 100, (progressContext) -> System.out.println(String.format("%s%% (%s)", progressContext.getWorkDoneInPercentage(), progressContext.getCurrentStep())));
    // IProgressMonitor progressMonitor = new NullProgressMonitor();
    modelImporter.parse(progressMonitor);
  }
}
