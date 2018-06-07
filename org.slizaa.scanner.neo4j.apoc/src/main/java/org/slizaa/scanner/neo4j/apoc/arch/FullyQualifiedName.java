package org.slizaa.scanner.neo4j.apoc.arch;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class FullyQualifiedName {

  /** - */
  private String             _fqn;

  /** - */
  private String[]           _parts;

  /** - */
  private FullyQualifiedName _parent;

  /**
   * <p>
   * Creates a new instance of type {@link FullyQualifiedName}.
   * </p>
   *
   * @param fqn
   */
  public FullyQualifiedName(String fqn) {
    _fqn = checkNotNull(fqn);
    _parts = splitFqn(fqn);
  }

  /**
   * <p>
   * Creates a new instance of type {@link FullyQualifiedName}.
   * </p>
   *
   * @param fqn
   */
  public FullyQualifiedName(String[] fqn) {
    _parts = checkNotNull(fqn);
    _fqn = concatFqn(_parts);
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public FullyQualifiedName getParent() {

    if (_parent == null && hasParent()) {
      _parent = new FullyQualifiedName(Arrays.copyOf(_parts, _parts.length - 1));
    }

    return _parent;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public boolean hasParent() {
    return _parts.length > 1;
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public String getSimpleName() {
    return _parts[_parts.length - 1];
  }


  public String getFullyQualifiedName() {
    return _fqn;
  }
  
  /**
   * <p>
   * </p>
   *
   * @return
   */
  public String[] getParts() {
    return _parts;
  }

  @Override
  public String toString() {
    return _fqn;
  }

  /**
   * <p>
   * </p>
   *
   * @param fqn
   * @return
   */
  private static String[] splitFqn(String fqn) {

    //
    while (checkNotNull(fqn).endsWith("/")) {
      fqn = fqn.substring(0, fqn.length() - 1);
    }

    //
    while (fqn.startsWith("/")) {
      fqn = fqn.substring(1, fqn.length());
    }

    //
    return fqn.split("/");
  }

  /**
   * <p>
   * </p>
   *
   * @param fqn
   * @return
   */
  private static String concatFqn(String[] fqn) {

    //
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < checkNotNull(fqn).length; i++) {
      stringBuilder.append(fqn[i]);
      if (i + 1 < checkNotNull(fqn).length) {
        stringBuilder.append("/");
      }
    }
    return stringBuilder.toString();
  }
}
