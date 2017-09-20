package org.slizaa.scanner.core.impl.plugins;

import java.util.List;

import org.slizaa.scanner.spi.parser.IParserFactory;

public interface ISlizaaPluginRegistry {

  List<Class<?>> getNeo4jExtensions();

  List<Class<? extends IParserFactory>> getParserFactories();
}
