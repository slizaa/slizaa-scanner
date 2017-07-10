package org.slizaa.scanner.jtype.bytecode.util;

import java.util.function.Supplier;

public class ContentCreator {

  public static final ContentDefinitions createSingleEntryContentDefinition(String directory, String name,
      Supplier<byte[]> supplier) {

    //
    ContentDefinitions definitions = new ContentDefinitions();
    ContentDefinition definition = new ContentDefinition();
    definitions.getContentDefinitions().add(definition);

    //
    Resource resource = new Resource(directory, name, supplier);
    definition.getBinaryResources().add(resource);

    //
    return definitions;
  }
}
