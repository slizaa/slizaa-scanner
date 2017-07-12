package org.slizaa.scanner.api.graphdb;

import java.io.File;

public interface IGraphDbFactory {

  <T> IGraphDb createGraphDb(T userObject, int Port, File storeDir);
}
