package org.slizaa.scanner.core.impl.plugins;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.Manifest;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public class SlizaaPluginUtils {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public static List<URL> getExtensionsFromClasspath() {

    List<URL> result = new LinkedList<URL>();

    try {
      Enumeration<URL> extensions = SlizaaPluginRegistry.class.getClassLoader().getResources("META-INF/MANIFEST.MF");

      while (extensions.hasMoreElements()) {

        //
        URL url = extensions.nextElement();
        //
        try (InputStream stream = url.openStream()) {

          //
          Manifest manifest = new Manifest(stream);

          String value = manifest.getMainAttributes().getValue(ISlizaaPluginRegistry.SLIZAA_EXTENSION);
          System.out.println(value);
          if (Boolean.parseBoolean(value)) {

            //
            String path = url.toString();
            if (url.getProtocol().equals("jar")) {
              path = url.getFile();
            }
            if (path.endsWith("/META-INF/MANIFEST.MF")) {
              path = path.substring(0, path.length() - "/META-INF/MANIFEST.MF".length());
            }
            if (path.endsWith("!")) {
              path = path.substring(0, path.length() - "!".length());
            }

            //
            try {
              result.add(new URI(path).toURL());
            } catch (URISyntaxException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }
        }

      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return result;
  }

}
