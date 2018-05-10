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

import java.util.LinkedList;
import java.util.List;

import org.neo4j.procedure.Procedure;
import org.neo4j.procedure.UserFunction;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.slizaa.scanner.core.api.graphdb.IGraphDbFactory;
import org.slizaa.scanner.core.api.importer.IModelImporterFactory;
import org.slizaa.scanner.core.classpathscanner.IClasspathScannerService;
import org.slizaa.scanner.neo4j.graphdbfactory.GraphDbFactory;
import org.slizaa.scanner.neo4j.importer.ModelImporterFactory;

/**
 */
public class Activator implements BundleActivator {

  /** - */
  private ServiceTracker<IClasspathScannerService, IClasspathScannerService> _classpathScannerService;

  @Override
  public void start(BundleContext context) throws Exception {

    this._classpathScannerService = new ServiceTracker<>(context, IClasspathScannerService.class, null);
    this._classpathScannerService.open();

    //
    context.registerService(IModelImporterFactory.class.getName(), new ModelImporterFactory(), null);
    context.registerService(IGraphDbFactory.class.getName(), new GraphDbFactory(() -> {

      //
      IClasspathScannerService classpathScannerService = this._classpathScannerService.getService();

      //
      if (classpathScannerService != null) {
        List<Class<?>> result = new LinkedList<>();
        result.addAll(classpathScannerService.getExtensionsWithMethodAnnotation(Procedure.class));
        result.addAll(classpathScannerService.getExtensionsWithMethodAnnotation(UserFunction.class));
        return result;
      }
      return null;

    }), null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void stop(BundleContext context) throws Exception {
    this._classpathScannerService.close();
  }
}
