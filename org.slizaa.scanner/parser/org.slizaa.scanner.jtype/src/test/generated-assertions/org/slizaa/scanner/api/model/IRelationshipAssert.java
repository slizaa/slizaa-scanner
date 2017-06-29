package org.slizaa.scanner.api.model;


/**
 * {@link IRelationship} specific assertions - Generated by CustomAssertionGenerator.
 *
 * Although this class is not final to allow Soft assertions proxy, if you wish to extend it, 
 * extend {@link AbstractIRelationshipAssert} instead.
 */
public class IRelationshipAssert extends AbstractIRelationshipAssert<IRelationshipAssert, IRelationship> {

  /**
   * Creates a new <code>{@link IRelationshipAssert}</code> to make assertions on actual IRelationship.
   * @param actual the IRelationship we want to make assertions on.
   */
  public IRelationshipAssert(IRelationship actual) {
    super(actual, IRelationshipAssert.class);
  }

  /**
   * An entry point for IRelationshipAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one can write directly: <code>assertThat(myIRelationship)</code> and get specific assertion with code completion.
   * @param actual the IRelationship we want to make assertions on.
   * @return a new <code>{@link IRelationshipAssert}</code>
   */
  public static IRelationshipAssert assertThat(IRelationship actual) {
    return new IRelationshipAssert(actual);
  }
}
