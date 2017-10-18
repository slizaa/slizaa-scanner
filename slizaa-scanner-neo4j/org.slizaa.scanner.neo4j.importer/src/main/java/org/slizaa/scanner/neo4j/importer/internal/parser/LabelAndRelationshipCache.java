/*******************************************************************************
 * Copyright (C) 2017 wuetherich
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.slizaa.scanner.neo4j.importer.internal.parser;

import static com.google.common.base.Preconditions.checkNotNull;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class LabelAndRelationshipCache {

  /** the instance */
  private static LabelAndRelationshipCache                         _instance              = new LabelAndRelationshipCache();

  /** - */
  private LoadingCache<String, Label>            _labelCache            = CacheBuilder.newBuilder()
      .build(new CacheLoader<String, Label>() {
                                                                              public Label load(String key) {
                                                                                return Label.label(key);
                                                                              }
                                                                            });

  /** - */
  private LoadingCache<String, RelationshipType> _relationshipTypeCache = CacheBuilder.newBuilder()
      .build(new CacheLoader<String, RelationshipType>() {
                                                                              public RelationshipType load(String key) {
                                                                                return RelationshipType.withName(key);
                                                                              }
                                                                            });

  public static LabelAndRelationshipCache instance() {
    return _instance;
  }

  public static Label convert(org.slizaa.scanner.core.spi.parser.model.Label slizaaLabel) {
    return instance().getLabel(slizaaLabel);
  }

  /**
   * <p>
   * </p>
   *
   * @param slizaaLabels
   * @return
   */
  public static Label[] convert(org.slizaa.scanner.core.spi.parser.model.Label[] slizaaLabels) {

    //
    Label[] result = new Label[checkNotNull(slizaaLabels.length)];

    //
    for (int i = 0; i < slizaaLabels.length; i++) {
      result[i] = convert(slizaaLabels[i]);
    }

    //
    return result;
  }

  public static RelationshipType convert(org.slizaa.scanner.core.spi.parser.model.RelationshipType relationshipType) {
    return instance().getRelationship(relationshipType);
  }

  /**
   * <p>
   * </p>
   *
   * @param slizaaLabel
   * @return
   */
  public Label getLabel(org.slizaa.scanner.core.spi.parser.model.Label slizaaLabel) {
    return _labelCache.getUnchecked(checkNotNull(slizaaLabel.name()));
  }

  public RelationshipType getRelationship(org.slizaa.scanner.core.spi.parser.model.RelationshipType relationshipType) {
    return _relationshipTypeCache.getUnchecked(checkNotNull(relationshipType.name()));
  }
}
