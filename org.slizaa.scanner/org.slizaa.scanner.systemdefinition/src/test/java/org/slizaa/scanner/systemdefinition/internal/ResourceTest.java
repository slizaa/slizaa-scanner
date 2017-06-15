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
package org.slizaa.scanner.systemdefinition.internal;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.slizaa.scanner.importer.spi.content.IResource;

public class ResourceTest {

  @Test
  public void testResource() {

    // classes directory
    String rootDir = System.getProperty("user.dir").replace('\\', '/') + "/target/classes";

    IResource resource = new Resource(rootDir, Resource.class.getName().replace('.', '/') + ".class");

    assertThat(resource.getRoot(), is(rootDir));
    assertThat(resource.getName(), is("Resource.class"));
    assertThat(resource.getPath(), is("org/slizaa/scanner/systemdefinition/internal/Resource.class"));
    assertThat(resource.getDirectory(), is("org/slizaa/scanner/systemdefinition/internal"));
  }
}
