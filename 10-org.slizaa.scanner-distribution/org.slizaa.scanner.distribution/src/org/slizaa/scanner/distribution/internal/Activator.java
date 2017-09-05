package org.slizaa.scanner.distribution.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slizaa.scanner.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.api.importer.IModelImporterFactory;
import org.slizaa.scanner.importer.ModelImporterFactory;
import org.slizaa.scanner.jtype.bytecode.JTypeByteCodeParserFactory;
import org.slizaa.scanner.spi.parser.IParserFactory;

public class Activator implements BundleActivator {

  @Override
  public void start(BundleContext context) throws Exception {
    context.registerService(IModelImporterFactory.class, new ModelImporterFactory(), null);
    context.registerService(IParserFactory.class, new JTypeByteCodeParserFactory(), null);
    context.registerService(IGraphDbFactory.class, new GraphDbFactory(), null);
  }

  @Override
  public void stop(BundleContext context) throws Exception {

  }
}
