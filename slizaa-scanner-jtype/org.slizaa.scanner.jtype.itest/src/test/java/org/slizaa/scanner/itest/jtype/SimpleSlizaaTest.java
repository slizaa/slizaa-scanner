package org.slizaa.scanner.itest.jtype;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.harness.junit.SlizaaClientRule;
import org.neo4j.harness.junit.SlizaaTestServerRule;
import org.slizaa.scanner.jtype.bytecode.JTypeByteCodeParserFactory;
import org.slizaa.scanner.spi.content.AnalyzeMode;
import org.slizaa.scanner.spi.content.IContentDefinitions;
import org.slizaa.scanner.spi.content.ResourceType;
import org.slizaa.scanner.systemdefinition.FileBasedContentDefinitionProvider;
import org.slizaa.scanner.systemdefinition.ISystemDefinition;
import org.slizaa.scanner.systemdefinition.SystemDefinitionFactory;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SimpleSlizaaTest {

  @ClassRule
  public static SlizaaTestServerRule slizaaTestServerRule = new SlizaaTestServerRule()
      // the content defintions
      .withContentDefinitions(getContentDefinitions())
      // the parser factories
      .withParserFactory(JTypeByteCodeParserFactory.class);

  @Rule
  public SlizaaClientRule            _client              = new SlizaaClientRule(slizaaTestServerRule);

  /**
   * <p>
   * </p>
   */
  @Test
  public void test() {

    //
    StatementResult statementResult = _client.getSession().run("Match (m:MODULE)-[:CONTAINS]->(d:DIRECTORY) return d");
    statementResult.forEachRemaining(map -> System.out.println(map.get("d").asNode()));
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  protected static IContentDefinitions getContentDefinitions() {

    //
    ISystemDefinition systemDefinition = new SystemDefinitionFactory().createNewSystemDefinition();

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider = new FileBasedContentDefinitionProvider("jtype", "1.2.3",
        AnalyzeMode.BINARIES_ONLY);
    provider.addRootPath(AbstractJTypeParserTest.class.getProtectionDomain().getCodeSource().getLocation().getFile(),
        ResourceType.BINARY);
    systemDefinition.addContentDefinitionProvider(provider);

    // initialize
    systemDefinition.initialize(null);

    //
    return systemDefinition;
  }

}
