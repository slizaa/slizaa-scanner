package org.slizaa.scanner.jtype.bytecode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Supplier;

import org.slizaa.scanner.core.spi.parser.IParser;
import org.slizaa.scanner.core.spi.parser.IParserContext;
import org.slizaa.scanner.core.spi.parser.model.INode;
import org.slizaa.scanner.core.spi.parser.model.INode;
import org.slizaa.scanner.core.spi.parser.model.NodeFactory;
import org.slizaa.scanner.core.spi.parser.model.resource.CoreModelRelationshipType;
import org.slizaa.scanner.jtype.bytecode.util.ContentCreator;
import org.slizaa.scanner.jtype.bytecode.util.ContentDefinitions;
import org.slizaa.scanner.jtype.bytecode.util.PrimitiveDatatypeNodeProvider;

import net.bytebuddy.dynamic.DynamicType.Builder;

public abstract class AbstractByteBuddyBytecodeTest {

  /**
   * <p>
   * </p>
   *
   * @param supplier
   * @return
   */
  public static INode parse(Supplier<Builder<?>> supplier) {
    return parseType("Type", "example", () -> supplier.get().name("example.Type").make().getBytes());
  }

  /**
   * <p>
   * </p>
   * 
   * @param name
   * @param directory
   * @param supplier
   *
   * @return
   */
  public static INode parseType(String name, String directory, Supplier<byte[]> supplier) {

    //
    ContentDefinitions definitions = ContentCreator.createSingleEntryContentDefinition(directory, name, supplier);

    JTypeByteCodeParserFactory parserFactory = new JTypeByteCodeParserFactory();
    parserFactory._datatypeNodeProvider = new PrimitiveDatatypeNodeProvider();

    IParser codeParser = parserFactory.createParser(definitions);

    INode resourceBean = NodeFactory.createNode();
    INode directoryBean = NodeFactory.createNode();
    INode moduleBean = NodeFactory.createNode();

    codeParser.parseResource(definitions.getContentDefinitions().get(0),
        definitions.getContentDefinitions().get(0).getBinaryFiles().iterator().next(), resourceBean,
        new IParserContext() {
          @Override
          public boolean parseReferences() {
            return true;
          }

          @Override
          public INode getParentDirectoryNode() {
            return directoryBean;
          }

          @Override
          public INode getParentModuleNode() {
            return moduleBean;
          }
        });

    INode node = resourceBean.getRelationships().get(CoreModelRelationshipType.CONTAINS).get(0).getTargetBean();
    assertThat(node).isNotNull();
    return node;
  }
}
