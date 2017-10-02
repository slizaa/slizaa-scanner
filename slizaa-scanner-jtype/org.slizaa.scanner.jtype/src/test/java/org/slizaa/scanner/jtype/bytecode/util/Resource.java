package org.slizaa.scanner.jtype.bytecode.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.function.Supplier;

import org.slizaa.scanner.core.spi.contentdefinition.IResource;

public class Resource implements IResource {

  /** - */
  private String           _directory;

  /** - */
  private String           _name;

  /** - */
  private Supplier<byte[]> _supplier;

  /**
   * <p>
   * Creates a new instance of type {@link Resource}.
   * </p>
   *
   * @param directory
   * @param name
   * @param supplier
   */
  public Resource(String directory, String name, Supplier<byte[]> supplier) {
    _directory = checkNotNull(directory);
    _name = checkNotNull(name);
    _supplier = checkNotNull(supplier);
  }

  @Override
  public String getRoot() {
    return "test.zip";
  }

  @Override
  public String getPath() {
    return getDirectory() + "/" + getName();
  }

  @Override
  public String getDirectory() {
    return _directory;
  }

  @Override
  public String getName() {
    return _name;
  }

  @Override
  public byte[] getContent() {
    return _supplier.get();
  }
}
