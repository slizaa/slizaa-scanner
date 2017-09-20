package org.slizaa.scanner.api.graphdb;

public interface IGraphDb extends AutoCloseable {

  <T> T getUserObject(Class<T> userObject);

  <T> boolean hasUserObject(Class<T> userObject);

  int getPort();

  void shutdown();
}
