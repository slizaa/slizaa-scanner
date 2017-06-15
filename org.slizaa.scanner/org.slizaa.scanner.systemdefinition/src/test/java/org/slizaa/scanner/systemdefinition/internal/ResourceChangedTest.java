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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.slizaa.scanner.systemdefinition.TestHelper.createContentDefinitionProvider;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;
import org.slizaa.scanner.importer.spi.content.IContentDefinition;
import org.slizaa.scanner.importer.spi.content.IResource;
import org.slizaa.scanner.model.resource.ResourceType;
import org.slizaa.scanner.systemdefinition.AbstractContentDefinitionProvider;
import org.slizaa.scanner.systemdefinition.FileBasedContentDefinitionProvider;
import org.slizaa.scanner.systemdefinition.ResourceChangedEvent;

public class ResourceChangedTest extends AbstractSystemChangedTest {

  @Test
  public void testResourceAddedRemoved() {

    //
    FileBasedContentDefinitionProvider provider = (FileBasedContentDefinitionProvider) createContentDefinitionProvider();

    //
    systemDefinition().addContentDefinitionProvider(provider);
    assertThat(systemDefinitionChangedEvents()).hasSize(1);

    //
    new FileBasedContentDefinitionProviderRaper(provider).handleResourceAdded(provider.getFileBasedContent(), "bla",
        "blubb", ResourceType.BINARY);
    assertThat(resourceChangedEvents()).hasSize(1);
    assertThat(resourceChangedEvents().get(0).getSystemDefinition()).isEqualTo(systemDefinition());
    assertThat(resourceChangedEvents().get(0).getContentDefinition()).isEqualTo(provider.getFileBasedContent());
    assertThat(resourceChangedEvents().get(0).getResource().getRoot()).isEqualTo("bla");
    assertThat(resourceChangedEvents().get(0).getResource().getPath()).isEqualTo("blubb");
    assertThat(resourceChangedEvents().get(0).getType()).isEqualTo(ResourceChangedEvent.Type.ADDED);

    new FileBasedContentDefinitionProviderRaper(provider).handleResourceRemoved(provider.getFileBasedContent(),
        resourceChangedEvents().get(0).getResource(), ResourceType.BINARY);

    //
    systemDefinition().removeContentDefinitionProvider(provider);
    assertThat(resourceChangedEvents()).hasSize(2);
  }

  /**
   * <p>
   * </p>
   * 
   * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
   */
  private static class FileBasedContentDefinitionProviderRaper {

    /** - */
    private FileBasedContentDefinitionProvider _provider;

    /**
     * <p>
     * Creates a new instance of type {@link FileBasedContentDefinitionProviderRaper}.
     * </p>
     * 
     * @param provider
     */
    public FileBasedContentDefinitionProviderRaper(FileBasedContentDefinitionProvider provider) {
      Assert.assertNotNull(provider);

      //
      _provider = provider;
    }

    /**
     * <p>
     * </p>
     * 
     * @param contentDefinition
     * @param root
     * @param path
     * @param type
     */
    public void handleResourceAdded(IContentDefinition contentDefinition, String root, String path, ResourceType type) {

      try {

        //
        Method method = AbstractContentDefinitionProvider.class.getDeclaredMethod("handleResourceAdded",
            IContentDefinition.class, String.class, String.class, ResourceType.class);
        method.setAccessible(true);
        method.invoke(_provider, contentDefinition, root, path, type);

      } catch (Exception e) {
        e.printStackTrace();
        fail(e.getMessage());
      }
    }

    /**
     * <p>
     * </p>
     * 
     * @param contentDefinition
     * @param root
     * @param path
     * @param type
     */
    public void handleResourceRemoved(IContentDefinition contentDefinition, IResource resource, ResourceType type) {

      try {

        //
        Method method = AbstractContentDefinitionProvider.class.getDeclaredMethod("handleResourceRemoved",
            IContentDefinition.class, IResource.class, ResourceType.class);
        method.setAccessible(true);
        method.invoke(_provider, contentDefinition, resource, type);

      } catch (Exception e) {
        e.printStackTrace();
        fail(e.getMessage());
      }
    }
  }
}
