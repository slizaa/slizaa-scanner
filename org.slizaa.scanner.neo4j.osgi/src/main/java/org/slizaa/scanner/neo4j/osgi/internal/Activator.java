/*******************************************************************************
 * Copyright (C) 2017 Gerd Wuetherich
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package org.slizaa.scanner.neo4j.osgi.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slizaa.scanner.core.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.core.api.importer.IModelImporterFactory;
import org.slizaa.scanner.neo4j.graphdbfactory.internal.GraphDbFactory;
import org.slizaa.scanner.neo4j.importer.internal.ModelImporterFactory;

/**
 */
public class Activator implements BundleActivator {

  @Override
  public void start(final BundleContext context) throws Exception {

    //
    context.registerService(IModelImporterFactory.class.getName(), new ModelImporterFactory(), null);
    context.registerService(IGraphDbFactory.class.getName(),
        new GraphDbFactory(/* resolveExtensionsSupplier(context) */), null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stop(BundleContext context) throws Exception {
    //
  }

  // /**
  // * <p>
  // * </p>
  // *
  // * @param context
  // * @return
  // */
  // private Supplier<List<Class<?>>> resolveExtensionsSupplier(BundleContext context) {
  //
  // //
  // return () -> {
  //
  // //
  // List<Class<?>> result = new ArrayList<Class<?>>();
  //
  // //
  // Enumeration<URL> apocLists = context.getBundle().findEntries("/", "apoc.list", false);
  //
  // //
  // while (apocLists.hasMoreElements()) {
  //
  // //
  // URL url = apocLists.nextElement();
  //
  // //
  // try (InputStream stream = url.openStream()) {
  //
  // //
  // List<Class<?>> classesList = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).lines()
  // .map(className -> {
  // try {
  // return context.getBundle().loadClass(className);
  // } catch (Exception e) {
  // return null;
  // }
  // }).filter(v -> v != null).collect(Collectors.toList());
  //
  // //
  // result.addAll(classesList);
  // }
  // //
  // catch (Exception e) {
  // e.printStackTrace();
  // }
  // }
  //
  // //
  // return result;
  // };
  // }
}
