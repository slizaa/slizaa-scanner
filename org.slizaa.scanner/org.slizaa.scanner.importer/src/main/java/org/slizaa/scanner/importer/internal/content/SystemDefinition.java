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
package org.slizaa.scanner.importer.internal.content;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slizaa.scanner.importer.content.AbstractContentDefinitionProvider;
import org.slizaa.scanner.importer.content.IContentDefinition;
import org.slizaa.scanner.importer.content.IContentDefinitionProvider;
import org.slizaa.scanner.importer.content.IResourceChangedListener;
import org.slizaa.scanner.importer.content.ISystemDefinition;
import org.slizaa.scanner.importer.content.ISystemDefinitionChangedListener;
import org.slizaa.scanner.importer.content.ResourceChangedEvent;
import org.slizaa.scanner.importer.content.SystemDefinitionChangedEvent;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SystemDefinition implements ISystemDefinition {

  /** - */
  private static NumberFormat                    FORMATTER  = new DecimalFormat("0000000000");

  /** the current identifier */
  @Expose
  @SerializedName("current-id")
  private int                                    _currentId = 0;

  /** - */
  @Expose
  @SerializedName("project-content-providers")
  private List<IContentDefinitionProvider>       _projectContentProviders;

  /** - */
  private Object                                 _identifierLock;

  /** - */
  private List<IContentDefinition>               _contentDefinitions;

  /** - */
  private boolean                                _initialized;

  /** - */
  private List<ISystemDefinitionChangedListener> _systemDefinitionChangedListener;

  /** - */
  private List<IResourceChangedListener>         _resourceChangedListeners;

  /**
   * <p>
   * Creates a new instance of type {@link SystemDefinition}.
   * </p>
   *
   * @param currentId
   */
  public SystemDefinition(int currentId, List<IContentDefinitionProvider> contentDefinitionProviders) {
    checkNotNull(contentDefinitionProviders);

    //
    _currentId = currentId;
    _identifierLock = new Object();
    _initialized = false;

    //
    _contentDefinitions = new ArrayList<IContentDefinition>();
    _projectContentProviders = new ArrayList<IContentDefinitionProvider>();
    for (IContentDefinitionProvider provider : contentDefinitionProviders) {
      IContentDefinitionProvider copy = provider;
      if (copy instanceof AbstractContentDefinitionProvider) {
        ((AbstractContentDefinitionProvider) copy).setSystemDefinition(this);
      }
      _projectContentProviders.add(copy);
    }
    
    //
    _systemDefinitionChangedListener = new CopyOnWriteArrayList<>();
    _resourceChangedListeners = new CopyOnWriteArrayList<>();
  }

  /**
   * <p>
   * Creates a new instance of type {@link SystemDefinition}.
   * </p>
   */
  public SystemDefinition() {

    //
    _currentId = 0;
    _identifierLock = new Object();
    _initialized = false;

    //
    _contentDefinitions = new ArrayList<IContentDefinition>();
    _projectContentProviders = new ArrayList<>();

    //
    _systemDefinitionChangedListener = new CopyOnWriteArrayList<>();
    _resourceChangedListeners = new CopyOnWriteArrayList<>();
  }

  /**
   * <p>
   * Copy constructor.
   * </p>
   * 
   * @param systemDefinition
   */
  SystemDefinition(SystemDefinition systemDefinition) {
    checkNotNull(systemDefinition);

    //
    _currentId = systemDefinition._currentId;
    _identifierLock = new Object();
    _initialized = false;

    //
    _contentDefinitions = new ArrayList<IContentDefinition>();
    _projectContentProviders = new ArrayList<IContentDefinitionProvider>();
    for (IContentDefinitionProvider provider : systemDefinition._projectContentProviders) {
      IContentDefinitionProvider copy = provider.copyInstance();
      if (copy instanceof AbstractContentDefinitionProvider) {
        ((AbstractContentDefinitionProvider) copy).setSystemDefinition(this);
      }
      _projectContentProviders.add(copy);
    }

    //
    _systemDefinitionChangedListener = new CopyOnWriteArrayList<>();
    _resourceChangedListeners = new CopyOnWriteArrayList<>();
  }

  public void mergeValues(SystemDefinition systemDefinition) {
    checkNotNull(systemDefinition);

    //
    _initialized = false;
    _contentDefinitions.clear();

    //
    _currentId = systemDefinition._currentId;

    //
    Map<String, IContentDefinitionProvider> map = new HashMap<String, IContentDefinitionProvider>();
    for (IContentDefinitionProvider provider : _projectContentProviders) {
      map.put(provider.getId(), provider);
    }
    _projectContentProviders.clear();

    // add or merge providers
    for (IContentDefinitionProvider provider : systemDefinition._projectContentProviders) {

      if (!map.containsKey(provider.getId())) {
        _projectContentProviders.add(provider.copyInstance());
      } else {
        IContentDefinitionProvider p = map.get(provider.getId());
        p.merge(provider);
        _projectContentProviders.add(p);
      }
    }

    //
    fireSystemDefinitionChangedEvent(new SystemDefinitionChangedEvent());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addSystemChangedListener(ISystemDefinitionChangedListener listener) {
    _systemDefinitionChangedListener.add(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeSystemChangedListener(ISystemDefinitionChangedListener listener) {
    _systemDefinitionChangedListener.remove(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addResourceChangedListener(IResourceChangedListener changedListener) {
    _resourceChangedListeners.add(changedListener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeResourceChangedListener(IResourceChangedListener changedListener) {
    _resourceChangedListeners.remove(changedListener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<? extends IContentDefinitionProvider> getContentDefinitionProviders() {
    return Collections.unmodifiableList(_projectContentProviders);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IContentDefinition> getContentDefinitions() {

    //
    checkState(_initialized,
        String.format("Can not return content definition list: SystemDefinition '%s' has to be initialized.", this));

    //
    return Collections.unmodifiableList(_contentDefinitions);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addContentDefinitionProvider(IContentDefinitionProvider... contentProviders) {

    //
    checkNotNull(contentProviders);

    //
    for (IContentDefinitionProvider provider : contentProviders) {
      checkState(
          provider instanceof AbstractContentDefinitionProvider,
          String.format("IProjectContentProvider must be instance of %s.",
              AbstractContentDefinitionProvider.class.getName()));
    }

    //
    for (IContentDefinitionProvider provider : contentProviders) {

      if (provider != null && !_projectContentProviders.contains(provider)) {

        _initialized = false;
        ((AbstractContentDefinitionProvider) provider).setSystemDefinition(this);

        String id = getNextContentProviderId();
        ((AbstractContentDefinitionProvider) provider).setContentDefinitionProviderId(id);

        _projectContentProviders.add(provider);

        fireSystemDefinitionChangedEvent(new SystemDefinitionChangedEvent());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeContentDefinitionProvider(IContentDefinitionProvider... contentProviders) {

    //
    checkNotNull(contentProviders);

    //
    for (IContentDefinitionProvider provider : contentProviders) {

      if (provider != null && _projectContentProviders.contains(provider)) {

        //
        _initialized = false;
        ((AbstractContentDefinitionProvider) provider).setSystemDefinition(null);

        //
        _projectContentProviders.remove(provider);

        // fire event
        fireSystemDefinitionChangedEvent(new SystemDefinitionChangedEvent());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void moveUpContentDefinitionProvider(IContentDefinitionProvider... contentProviders) {

    // assert selected items are not null
    checkNotNull(contentProviders);

    // return if empty
    if (contentProviders.length == 0) {
      return;
    }

    //
    _initialized = false;

    // order the selected items
    List<IContentDefinitionProvider> orderSelectedItems = Arrays.asList(contentProviders);
    Collections.sort(orderSelectedItems, new Comparator<IContentDefinitionProvider>() {
      @Override
      public int compare(IContentDefinitionProvider o1, IContentDefinitionProvider o2) {
        return _projectContentProviders.indexOf(o1) - _projectContentProviders.indexOf(o2);
      }
    });

    try {
      // move the items up
      for (IContentDefinitionProvider provider : orderSelectedItems) {

        //
        int index = _projectContentProviders.indexOf(provider);
        if (index == 0) {
          return;
        }

        //
        _projectContentProviders.remove(provider);
        _projectContentProviders.add(index - 1, provider);
      }
    } finally {
      //
      fireSystemDefinitionChangedEvent(new SystemDefinitionChangedEvent());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void moveDownContentDefinitionProvider(IContentDefinitionProvider... contentProviders) {

    // assert selected items are not null
    checkNotNull(contentProviders);

    // return if empty
    if (contentProviders.length == 0) {
      return;
    }

    //
    _initialized = false;

    // order the selected items
    List<IContentDefinitionProvider> orderSelectedItems = Arrays.asList(contentProviders);
    Collections.sort(orderSelectedItems, new Comparator<IContentDefinitionProvider>() {
      @Override
      public int compare(IContentDefinitionProvider o1, IContentDefinitionProvider o2) {
        return _projectContentProviders.indexOf(o2) - _projectContentProviders.indexOf(o1);
      }
    });

    // move the items up
    try {
      for (IContentDefinitionProvider provider : orderSelectedItems) {

        //
        int index = _projectContentProviders.indexOf(provider);
        if (index == _projectContentProviders.size() - 1) {
          return;
        }

        //
        _projectContentProviders.remove(provider);
        _projectContentProviders.add(index + 1, provider);
      }
    } finally {
      fireSystemDefinitionChangedEvent(new SystemDefinitionChangedEvent());
    }
  }

  @Override
  public void removeContentDefinitionProvider(String... ids) {

    //
    checkNotNull(ids);

    //
    List<String> idList = Arrays.asList(ids);

    //
    List<IContentDefinitionProvider> providerToRemove = new LinkedList<>();

    //
    for (IContentDefinitionProvider provider : _projectContentProviders) {

      //
      if (idList.contains(provider.getId())) {
        providerToRemove.add(provider);
      }
    }

    //
    removeContentDefinitionProvider(providerToRemove.toArray(new IContentDefinitionProvider[0]));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clearContentDefinitionProviders() {
    synchronized (_identifierLock) {
      _contentDefinitions.clear();
      _projectContentProviders.clear();
      _currentId = 0;
      _initialized = false;
    }

    // fire event
    fireSystemDefinitionChangedEvent(new SystemDefinitionChangedEvent());
  }

  public String getNextContentProviderId() {
    synchronized (_identifierLock) {
      return FORMATTER.format(_currentId++);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void initialize(IProgressMonitor progressMonitor) {

    //
    if (_initialized) {
      return;
    }

    // clear the project content list
    _contentDefinitions.clear();

    for (IContentDefinitionProvider contentProvider : _projectContentProviders) {
      ((AbstractContentDefinitionProvider) contentProvider).prepare();
    }

    //
    for (IContentDefinitionProvider contentProvider : _projectContentProviders) {

      //
      ((AbstractContentDefinitionProvider) contentProvider).initializeProjectContent(null);

      //
      List<IContentDefinition> projectContents = contentProvider.getContentDefinitions();

      //
      _contentDefinitions.addAll(projectContents);
    }

    //
    _initialized = true;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isInitialized() {
    return _initialized;
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  public int getCurrentId() {
    synchronized (_identifierLock) {
      return _currentId;
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param currentId
   */
  public void setCurrentId(int currentId) {
    synchronized (_identifierLock) {
      _currentId = currentId;
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param event
   */
  public void fireContentChangedEvent(ResourceChangedEvent event) {
    checkNotNull(event);

    //
    for (IResourceChangedListener listener : _resourceChangedListeners) {
      try {
        listener.resourceChanged(event);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  /**
   * <p>
   * </p>
   * 
   * @param event
   */
  public void fireSystemDefinitionChangedEvent(SystemDefinitionChangedEvent event) {
    checkNotNull(event);

    // notify listeners
    for (ISystemDefinitionChangedListener systemChangedListener : _systemDefinitionChangedListener) {
      try {
        systemChangedListener.systemDefinitionChanged(event);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_projectContentProviders == null) ? 0 : _projectContentProviders.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SystemDefinition other = (SystemDefinition) obj;
    if (_projectContentProviders == null) {
      if (other._projectContentProviders != null)
        return false;
    } else if (!_projectContentProviders.equals(other._projectContentProviders))
      return false;
    return true;
  }

  /**
   * <p>
   * </p>
   * 
   * @param systemDefinitionChangedListener
   */
  void replaceAllSystemDefinitionChangedListeners(List<ISystemDefinitionChangedListener> systemDefinitionChangedListener) {
    checkNotNull(systemDefinitionChangedListener);

    _systemDefinitionChangedListener = new CopyOnWriteArrayList<>(systemDefinitionChangedListener);
  }

  /**
   * <p>
   * </p>
   * 
   * @param resourceChangedListeners
   */
  void replaceAllResourceChangedListeners(List<IResourceChangedListener> resourceChangedListeners) {
    checkNotNull(resourceChangedListeners);

    _resourceChangedListeners = new CopyOnWriteArrayList<>(resourceChangedListeners);
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  List<IResourceChangedListener> getAllResourceChangedListeners() {
    return new LinkedList<>(_resourceChangedListeners);
  }

  /**
   * <p>
   * </p>
   * 
   * @return
   */
  List<ISystemDefinitionChangedListener> getAllSystemDefinitionChangedListeners() {
    return new LinkedList<>(_systemDefinitionChangedListener);
  }

  /**
   * <p>
   * </p>
   */
  void clearResourceChangedListeners() {
    _resourceChangedListeners.clear();
  }

  /**
   * <p>
   * </p>
   */
  void clearSystemDefinitionChangedListeners() {
    _systemDefinitionChangedListener.clear();
  }

  public static class SystemDefinitionJsonDeserializer implements JsonDeserializer<SystemDefinition> {

    /**
     * {@inheritDoc}
     */
    public SystemDefinition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {

      //
      JsonObject jsonObject = (JsonObject) json;

      //
      JsonArray jsonArray = (JsonArray) jsonObject.get("project-content-providers");
      List<IContentDefinitionProvider> providers = new LinkedList<>();
      for (JsonElement jsonElement : jsonArray) {
        providers.add((IContentDefinitionProvider) context.deserialize(jsonElement, IContentDefinitionProvider.class));
      }

      //
      return new SystemDefinition(jsonObject.get("current-id").getAsInt(), providers);
    }
  }
}
