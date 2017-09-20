package org.slizaa.scanner.core.testfwk.junit;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.File;
import java.io.IOException;

import org.ops4j.pax.url.mvn.MavenResolvers;
import org.slizaa.scanner.spi.content.AnalyzeMode;
import org.slizaa.scanner.spi.content.ResourceType;
import org.slizaa.scanner.systemdefinition.FileBasedContentDefinitionProvider;
import org.slizaa.scanner.systemdefinition.ISystemDefinition;
import org.slizaa.scanner.systemdefinition.SystemDefinitionFactory;

public class ContentDefinitionsUtils {

  /**
   * <p>
   * </p>
   *
   * @param coordinates
   * @return
   */
  public static ISystemDefinition multipleBinaryMvnArtifacts(String[]... coordinates) {

    //
    checkNotNull(coordinates);
    for (String[] c : coordinates) {
      checkState(c.length == 3, "Coordinates must have three parts (groupId, artifactId, version)");
    }

    //
    try {

      //
      ISystemDefinition definition = new SystemDefinitionFactory().createNewSystemDefinition();

      // add new (custom) content provider
      
      for (String[] c : coordinates) {
        
        //
        File resolvedFile = MavenResolvers.createMavenResolver(null, null).resolve(checkNotNull(c[0]),
            checkNotNull(c[1]), null, "jar", checkNotNull(c[2]));
        definition.addContentDefinitionProvider(createContentDefinitionProvider(c[0], c[1], resolvedFile));
      }
      // initialize
      definition.initialize(null);

      //
      return definition;
      


    } catch (IOException e) {
      throw new RuntimeException(e);
    }
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
  public static ISystemDefinition simpleBinaryMvnArtifact(String groupId, String artifactId, String version) {

    try {

      //
      File resolvedFile = MavenResolvers.createMavenResolver(null, null).resolve(checkNotNull(groupId),
          checkNotNull(artifactId), null, "jar", checkNotNull(version));

      //
      return simpleBinaryFile(artifactId, version, resolvedFile);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
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
  public static ISystemDefinition simpleBinaryFile(String name, String version, String jarOrDirectory) {
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
  public static ISystemDefinition simpleBinaryFile(String name, String version, File jarOrDirectory) {

    checkNotNull(name);
    checkNotNull(version);
    checkNotNull(jarOrDirectory);

    //
    ISystemDefinition definition = new SystemDefinitionFactory().createNewSystemDefinition();

    // add new (custom) content provider
    definition.addContentDefinitionProvider(createContentDefinitionProvider(name, version, jarOrDirectory));

    // initialize
    definition.initialize(null);

    //
    return definition;
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
  private static FileBasedContentDefinitionProvider createContentDefinitionProvider(String name, String version,
      File jarOrDirectory) {
    
    FileBasedContentDefinitionProvider provider = new FileBasedContentDefinitionProvider(name, version,
        AnalyzeMode.BINARIES_ONLY);
    provider.addRootPath(jarOrDirectory, ResourceType.BINARY);
    
    return provider;
  }
}
