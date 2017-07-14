/*******************************************************************************
 * Copyright (c) 2011-2015 Slizaa project team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Slizaa project team - initial API and implementation
 ******************************************************************************/
package org.slizaa.scanner.jtype.bytecode;

import static com.google.common.base.Preconditions.checkNotNull;

import org.objectweb.asm.ClassReader;
import org.slizaa.scanner.api.model.IModifiableNode;
import org.slizaa.scanner.api.model.resource.CoreModelRelationshipType;
import org.slizaa.scanner.jtype.model.JTypeLabel;
import org.slizaa.scanner.jtype.model.JavaUtils;
import org.slizaa.scanner.jtype.model.internal.bytecode.JTypeClassVisitor;
import org.slizaa.scanner.jtype.model.internal.primitvedatatypes.IPrimitiveDatatypeNodeProvider;
import org.slizaa.scanner.spi.content.IContentDefinition;
import org.slizaa.scanner.spi.content.IResource;
import org.slizaa.scanner.spi.parser.AbstractParser;
import org.slizaa.scanner.spi.parser.IParser;
import org.slizaa.scanner.spi.parser.IParserContext;
import org.slizaa.scanner.spi.parser.IParserFactory;

/**
 * <p>
 * A {@link IParser} that parses java class files and creates a graph representation of the contained types.
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class JTypeByteCodeParser extends AbstractParser<JTypeByteCodeParserFactory> {

  /** the primitive datatype provider */
  private IPrimitiveDatatypeNodeProvider _primitiveDatatypeNodeProvider;

  /**
   * <p>
   * Creates a new instance of type {@link JTypeByteCodeParser}.
   * </p>
   * 
   * @param parserFactory
   *          the {@link IParserFactory} that created this {@link IParser}.
   * @param datatypeNodeProvider
   *          the {@link IPrimitiveDatatypeNodeProvider} for this parser
   */
  JTypeByteCodeParser(JTypeByteCodeParserFactory parserFactory, IPrimitiveDatatypeNodeProvider datatypeNodeProvider) {
    super(parserFactory);

    _primitiveDatatypeNodeProvider = checkNotNull(datatypeNodeProvider);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ParserType getParserType() {
    return ParserType.BINARY;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canParse(IResource resource) {

    //
    if (!resource.getPath().endsWith(".class")) {
      return false;
    }

    //
    return JavaUtils.isValidJavaPackage(resource.getDirectory());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doParseResource(IContentDefinition content, IResource resource, IModifiableNode resourceBean,
      IParserContext context) {

    // tag parent directory as package
    if (!context.getParentDirectoryNode().getLabels().contains(JTypeLabel.PACKAGE)) {
      context.getParentDirectoryNode().addLabel(JTypeLabel.PACKAGE);
    }

    // add CLASSFILE label
    resourceBean.addLabel(JTypeLabel.CLASSFILE);

    // create the visitor...
    JTypeClassVisitor visitor = new JTypeClassVisitor(_primitiveDatatypeNodeProvider);

    // ...parse the class
    ClassReader reader = new ClassReader(resource.getContent());
    reader.accept(visitor, 0);

    // add the contained type
    resourceBean.addRelationship(visitor.getTypeBean(), CoreModelRelationshipType.CONTAINS);
  }
}
