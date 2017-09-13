package org.assertj;

/**
 * Entry point for assertions of different data types. Each method in this class is a static factory for the
 * type-specific assertion objects.
 */
public class Assertions {

  /**
   * Creates a new instance of <code>{@link org.slizaa.scanner.api.model.INodeAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static org.slizaa.scanner.api.model.INodeAssert assertThat(org.slizaa.scanner.api.model.INode actual) {
    return new org.slizaa.scanner.api.model.INodeAssert(actual);
  }

  /**
   * Creates a new instance of <code>{@link org.slizaa.scanner.api.model.IRelationshipAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created assertion object.
   */
  public static org.slizaa.scanner.api.model.IRelationshipAssert assertThat(org.slizaa.scanner.api.model.IRelationship actual) {
    return new org.slizaa.scanner.api.model.IRelationshipAssert(actual);
  }

  /**
   * Creates a new <code>{@link Assertions}</code>.
   */
  protected Assertions() {
    // empty
  }
}
