package org.slizaa.scanner.itest.jtype.bytecode.jdeps;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.slizaa.scanner.itest.framework.jdeps.IJDepsWrapper;
import org.slizaa.scanner.itest.jtype.bytecode.AbstractJTypeParserTest;
import org.slizaa.scanner.jtype.model.ITypeNode;
import org.slizaa.scanner.spi.content.AnalyzeMode;
import org.slizaa.scanner.spi.content.ResourceType;
import org.slizaa.scanner.systemdefinition.FileBasedContentDefinitionProvider;
import org.slizaa.scanner.systemdefinition.ISystemDefinition;
import org.slizaa.scanner.systemdefinition.SystemDefinitionFactory;

@Ignore
public class JdepsTest extends AbstractJTypeParserTest {

  /** - */
  private static Map<String, List<String>> _jdepAnalysis;

  /** - */
  private File                             file = new File(
      // "D:\\50-Development\\environments\\slizaa-master\\git\\slizaa-private\\03-org.slizaa.neo4j\\org.slizaa.neo4j.dbadapter\\bin");
      "D:\\50-Development\\environments\\slizaa-master\\ws\\TestReferenceProject\\bin");

  @Override
  public void before() {
    super.before();

    if (_jdepAnalysis == null) {
      IJDepsWrapper jDepsWrapper = IJDepsWrapper.Factory.create();
      _jdepAnalysis = jDepsWrapper.analyze(file.getAbsolutePath());
    }
  }

  @Override
  public void after() {
    super.after();
  }

  @Test
  public void test() {
    _jdepAnalysis.keySet().stream().sorted().forEach(fqn -> assertSameReferences(fqn));
  }

  private void assertSameReferences(String fqn) {
    //
    List<Node> nodes = getNodes(
        executeStatement("Match (t:TYPE {fqn: $name})-[:DEPENDS_ON]->(tr:TYPE_REFERENCE) return tr",
            Collections.singletonMap("name", fqn)));

    //
    List<String> references = nodes.stream().map(node -> (String) node.getProperty(ITypeNode.FQN))
        .collect(Collectors.toList());
    System.out.println(fqn);
    assertThat(references).containsExactlyInAnyOrder(_jdepAnalysis.get(fqn).toArray(new String[0]));
  }

  @Override
  protected ISystemDefinition getSystemDefinition() {

    String name = "org.slizaa.neo4j.dbadapter";
    String version = "0.0.4-SNAPSHOT";

    //
    ISystemDefinition systemDefinition = new SystemDefinitionFactory().createNewSystemDefinition();

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider = new FileBasedContentDefinitionProvider(name, version,
        AnalyzeMode.BINARIES_ONLY);
    provider.addRootPath(file, ResourceType.BINARY);
    systemDefinition.addContentDefinitionProvider(provider);

    // initialize
    systemDefinition.initialize(null);

    //
    return systemDefinition;
  }
}
