package org.slizaa.scanner.jtype.bytecode;

import static org.assertj.Assertions.assertThat;

import java.util.function.Supplier;

import org.slizaa.scanner.api.model.IModifiableNode;
import org.slizaa.scanner.api.model.INode;
import org.slizaa.scanner.api.model.internal.NodeBean;
import org.slizaa.scanner.api.model.resource.CoreModelRelationshipType;
import org.slizaa.scanner.jtype.bytecode.util.ContentCreator;
import org.slizaa.scanner.jtype.bytecode.util.ContentDefinitions;
import org.slizaa.scanner.jtype.bytecode.util.PrimitiveDatatypeNodeProvider;
import org.slizaa.scanner.spi.parser.IParser;
import org.slizaa.scanner.spi.parser.IParserContext;

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
    parserFactory._datatypeNodeProviderMap.put(definitions, new PrimitiveDatatypeNodeProvider());

    IParser codeParser = parserFactory.createParser(definitions);

    IModifiableNode resourceBean = new NodeBean();
    IModifiableNode directoryBean = new NodeBean();
    IModifiableNode moduleBean = new NodeBean();
    
    codeParser.parseResource(definitions.getContentDefinitions().get(0),
        definitions.getContentDefinitions().get(0).getBinaryResources().iterator().next(), resourceBean,
        new IParserContext() {
          @Override
          public boolean parseReferences() {
            return true;
          }
          @Override
          public IModifiableNode getParentDirectoryNode() {
            return directoryBean;
          }
          @Override
          public IModifiableNode getParentModuleNode() {
            return moduleBean;
          }
        });

    INode node = resourceBean.getRelationships().get(CoreModelRelationshipType.CONTAINS).get(0).getTargetBean();
    assertThat(node).isNotNull();
    return node;
  }
}
