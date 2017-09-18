package org.slizaa.scanner.assembly.itest.eclipse.aether;

import java.io.File;

import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;

/**
 * <p>
 * https://github.com/eclipse/aether-demo
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class ResolveArtifact {

  /**
   * <p>
   * </p>
   *
   * @param groupId
   * @param artifactId
   * @param version
   * @return
   * @throws ArtifactResolutionException
   */
  public static File resolve(String groupId, String artifactId, String version) {

    try {
      //
      RepositorySystem system = Booter.newRepositorySystem();
      RepositorySystemSession session = Booter.newRepositorySystemSession(system);

      //
      Artifact artifact = new DefaultArtifact(String.format("%s:%s:%s", groupId, artifactId, version));

      ArtifactRequest artifactRequest = new ArtifactRequest();
      artifactRequest.setArtifact(artifact);
      artifactRequest.setRepositories(Booter.newRepositories(system, session));
      ArtifactResult artifactResult = system.resolveArtifact(session, artifactRequest);

      //
      return artifactResult.getArtifact().getFile();
    }
    //
    catch (ArtifactResolutionException e) {
      throw new RuntimeException(e);
    }
  }
}