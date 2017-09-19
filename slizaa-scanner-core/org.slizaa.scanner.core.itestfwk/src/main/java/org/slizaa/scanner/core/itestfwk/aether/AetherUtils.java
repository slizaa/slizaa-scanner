package org.slizaa.scanner.core.itestfwk.aether;

import java.io.File;

import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.slizaa.scanner.core.itestfwk.aether.internal.Booter;

public class AetherUtils {

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
  public static File resolve(String groupId, String artifactId, String version, String classifier, String extension) {

    try {
      
      //
      RepositorySystem system = Booter.newRepositorySystem();
      RepositorySystemSession session = Booter.newRepositorySystemSession(system);

      //
      Artifact artifact = new DefaultArtifact(groupId, artifactId, classifier, extension, version);

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
