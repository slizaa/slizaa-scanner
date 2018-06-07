package org.slizaa.scanner.neo4j.apoc;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.slizaa.scanner.neo4j.apoc.arch.FullyQualifiedName;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class FullyQualifiedNameTest {

  /**
   * <p>
   * </p>
   */
  @Test
  public void testFullyQualifiedName() {

    //
    FullyQualifiedName fqn_1 = new FullyQualifiedName("test/fest/pest");
    assertThat(fqn_1.getSimpleName()).isEqualTo("pest");
    
    //
    assertThat(fqn_1.getParent()).isNotNull();
    assertThat(fqn_1.getParent().toString()).isEqualTo("test/fest");
    assertThat(fqn_1.getParent().getSimpleName()).isEqualTo("fest");
    
    //
    assertThat(fqn_1.getParent().getParent()).isNotNull();
    assertThat(fqn_1.getParent().getParent().toString()).isEqualTo("test");
    assertThat(fqn_1.getParent().getParent().getSimpleName()).isEqualTo("test");
    
    //
    assertThat(fqn_1.getParent().getParent().getParent()).isNull();
  }
}
