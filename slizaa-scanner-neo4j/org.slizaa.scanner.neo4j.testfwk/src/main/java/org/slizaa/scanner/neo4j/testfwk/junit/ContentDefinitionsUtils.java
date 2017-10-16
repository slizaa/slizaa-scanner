package org.slizaa.scanner.neo4j.testfwk.junit;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.File;
import java.io.IOException;

import org.ops4j.pax.url.mvn.MavenResolvers;
import org.slizaa.scanner.core.contentdefinition.FileBasedContentDefinitionProvider;
import org.slizaa.scanner.core.contentdefinition.MvnBasedContentDefinitionProvider;
import org.slizaa.scanner.core.spi.contentdefinition.AnalyzeMode;
import org.slizaa.scanner.core.spi.contentdefinition.IContentDefinitionProvider;
import org.slizaa.scanner.core.spi.contentdefinition.ContentType;

public class ContentDefinitionsUtils {

  /**
   * <p>
   * </p>
   *
   * @param coordinates
   * @return
   */
  public static IContentDefinitionProvider multipleBinaryMvnArtifacts(String[]... coordinates) {

    //
    checkNotNull(coordinates);
    for (String[] c : coordinates) {
      checkState(c.length == 3, "Coordinates must have three parts (groupId, artifactId, version)");
    }

    //
    MvnBasedContentDefinitionProvider provider = new MvnBasedContentDefinitionProvider();

    //
    for (String[] coordinate : coordinates) {
      provider.addArtifact(checkNotNull(coordinate[0]), checkNotNull(coordinate[1]), checkNotNull(coordinate[2]));
    }

    //
    return provider;
  }

  /**
   * <p>
   * </p>
   *
   * @param groupId
   * @param artifactId
   * @param version
   * @return
   */
  public static IContentDefinitionProvider simpleBinaryMvnArtifact(String groupId, String artifactId, String version) {

    return multipleBinaryMvnArtifacts(new String[] { groupId, artifactId, version });
  }

  /**
   * <p>
   * </p>
   *
   * @param name
   * @param version
   * @param jarOrDirectory
   * @return
   */
  public static IContentDefinitionProvider simpleBinaryFile(String name, String version, String jarOrDirectory) {
    return simpleBinaryFile(name, version, new File(jarOrDirectory));
  }

  /**
   * <p>
   * </p>
   *
   * @param name
   * @param version
   * @param jarOrDirectory
   * @return
   */
  public static IContentDefinitionProvider simpleBinaryFile(String name, String version, File jarOrDirectory) {

    checkNotNull(name);
    checkNotNull(version);
    checkNotNull(jarOrDirectory);

    //
    FileBasedContentDefinitionProvider provider = new FileBasedContentDefinitionProvider();

    //
    provider.createFileBasedContentDefinition(name, version, new File[] { jarOrDirectory }, null,
        AnalyzeMode.BINARIES_ONLY);

    //
    return provider;
  }
}
