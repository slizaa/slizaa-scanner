package org.slizaa.scanner.itest.jtype.bytecode.jdeps;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.neo4j.graphdb.Node;
import org.slizaa.scanner.itest.framework.jdeps.IJDepsWrapper;
import org.slizaa.scanner.itest.framework.jdeps.internal.JavapWrapper;
import org.slizaa.scanner.itest.jtype.bytecode.AbstractJTypeParserTest;
import org.slizaa.scanner.jtype.model.ITypeNode;
import org.slizaa.scanner.spi.content.AnalyzeMode;
import org.slizaa.scanner.spi.content.ResourceType;
import org.slizaa.scanner.systemdefinition.FileBasedContentDefinitionProvider;
import org.slizaa.scanner.systemdefinition.ISystemDefinition;
import org.slizaa.scanner.systemdefinition.SystemDefinitionFactory;

@Ignore
@RunWith(Parameterized.class)
public class JdepsTest extends AbstractJTypeParserTest {

  /** - */
  private static Map<String, List<String>> _jdepAnalysis;

  private File                             _jarFile;

  public JdepsTest(File jarFile) {
    _jarFile = checkNotNull(jarFile);
  }

  @Override
  public void before() {
    super.before();

    if (_jdepAnalysis == null) {
      _jdepAnalysis = IJDepsWrapper.Factory.create().analyze(this._jarFile.getAbsolutePath());
    }
  }

  @Override
  public void after() {

    //
    super.after();

    //
    if (_jdepAnalysis != null) {
      System.out.printf("Successfully tested %s class files...", _jdepAnalysis.size());
      _jdepAnalysis.clear();
      _jdepAnalysis = null;
    }

    //
    _graphDb.shutdown();
    _graphDb = null;
  }

  /**
   * <p>
   * </p>
   */
  @Test
  public void test() {
    System.out.println("Testing " + _jarFile.getAbsolutePath());
    _jdepAnalysis.keySet().stream().sorted().forEach(fqn -> assertSameReferences(fqn));
    System.out.println("Done " + _jarFile.getAbsolutePath());
  }

  /**
   * <p>
   * </p>
   *
   * @param fqn
   */
  private void assertSameReferences(String fqn) {

    //
    List<Node> nodes = getNodes(
        executeStatement("Match (t:TYPE {fqn: $name})-[:DEPENDS_ON]->(tr:TYPE_REFERENCE) return tr",
            Collections.singletonMap("name", fqn)));

    //
    List<String> references = nodes.stream().map(node -> (String) node.getProperty(ITypeNode.FQN))
        .collect(Collectors.toList());

    //
    try {
      assertThat(references).containsExactlyInAnyOrder(_jdepAnalysis.get(fqn).toArray(new String[0]));
    } catch (AssertionError e) {
      JavapWrapper.doIt(_jarFile.getAbsolutePath(), fqn);
      throw e;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ISystemDefinition getSystemDefinition() {

    String name = "org.slizaa.neo4j.dbadapter";
    String version = "0.0.4-SNAPSHOT";

    //
    ISystemDefinition systemDefinition = new SystemDefinitionFactory().createNewSystemDefinition();

    // add new (custom) content provider
    FileBasedContentDefinitionProvider provider = new FileBasedContentDefinitionProvider(name, version,
        AnalyzeMode.BINARIES_ONLY);
    provider.addRootPath(_jarFile, ResourceType.BINARY);
    systemDefinition.addContentDefinitionProvider(provider);

    // initialize
    systemDefinition.initialize(null);

    //
    return systemDefinition;
  }

  @Parameters
  public static Collection<Object[]> data() {

    // the result
    Collection<Object[]> result = new ArrayList<>();

    String dir = "D:\\50-Development\\environments\\slizaa-master\\ws\\TestReferenceProject\\libs\\";

    try {
      Files.newDirectoryStream(Paths.get(dir), "*.jar").forEach(p -> result.add(new Object[] { p.toFile() }));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // return result
    return result;
  }
}
