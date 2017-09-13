package org.slizaa.scanner.core.eclipse.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slizaa.scanner.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.api.importer.IModelImporterFactory;
import org.slizaa.scanner.cmdline.server.GraphDbFactory;
import org.slizaa.scanner.importer.ModelImporterFactory;

public class Activator implements BundleActivator {

  @Override
  public void start(BundleContext context) throws Exception {
    context.registerService(IModelImporterFactory.class.getName(), new ModelImporterFactory(), null);
    context.registerService(IGraphDbFactory.class.getName(), new GraphDbFactory(), null);
  }

  @Override
  public void stop(BundleContext context) throws Exception {
  }
}
