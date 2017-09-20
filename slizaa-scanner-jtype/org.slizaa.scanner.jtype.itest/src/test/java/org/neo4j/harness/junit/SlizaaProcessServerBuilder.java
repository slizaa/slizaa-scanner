package org.neo4j.harness.junit;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.neo4j.harness.internal.AbstractInProcessServerBuilder;
import org.neo4j.kernel.configuration.Config;
import org.neo4j.kernel.impl.factory.GraphDatabaseFacadeFactory;
import org.neo4j.logging.FormattedLogProvider;
import org.neo4j.server.AbstractNeoServer;
import org.neo4j.server.CommunityNeoServer;

public class SlizaaProcessServerBuilder extends AbstractInProcessServerBuilder {

  public SlizaaProcessServerBuilder(File dataDir) {
    super(checkNotNull(dataDir).getParentFile(), dataDir.getName());
  }

  @Override
  protected AbstractNeoServer createNeoServer(Map<String, String> config,
      GraphDatabaseFacadeFactory.Dependencies dependencies, FormattedLogProvider userLogProvider) {
    return new CommunityNeoServer(Config.embeddedDefaults(config), dependencies, userLogProvider);
  }
}