package org.slizaa.scanner.cmdline.server;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.slizaa.scanner.spi.parser.IParserFactory;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.ImplementingClassMatchProcessor;

public class PluginRegistry {

  /** - */
  private Map<String, PluginDescriptor> _pluginDescriptors;

  /** - */
  private Gson                          _gson;

  /**
   * <p>
   * Creates a new instance of type {@link PluginRegistry}.
   * </p>
   *
   */
  public PluginRegistry() {

    //
    _pluginDescriptors = new HashMap<>();
    _gson = new Gson();
  }

  public void initialize() {

    new FastClasspathScanner("-org.slizaa.scanner.api", "-org.slizaa.scanner.spi", "-jar:neo4j-*.jar", "-jar:scala*.jar", "-jar:lucene*.jar", "-jar:lucene*.jar",
        "-jar:bcpkix*.jar", "-jar:bcprov*.jar", "-jar:caffeine*.jar", "-jar:common-*.jar", "-jar:commons-compress*.jar",
        "-jar:commons-lang3*.jar", "-jar:concurrentlinkedhashmap-lru-*.jar", "-jar:guava-*.jar",
        "-jar:jcommander-*.jar", "-jar:opencsv-*.jar", "-jar:parboiled-*.jar", "-jar:slf4j-*.jar", "-jar:netty-*.jar")
            // Add a MatchProcessor ("Mechanism 1")
//            .verbose()
            .matchClassesImplementing(IParserFactory.class, new ImplementingClassMatchProcessor<IParserFactory>() {
              @Override
              public void processMatch(Class<? extends IParserFactory> matchingClass) {
                System.out.println("Subclass of Widget: " + matchingClass);
              }
            })
            // Actually perform the scan (nothing will happen without this call)
            .scan();

    //
    // try {
    // Enumeration<URL> fileUrls = this.getClass().getClassLoader().getResources("slizaa-scanner-plugin.json");
    // while (fileUrls.hasMoreElements()) {
    // URL fileUrl = fileUrls.nextElement();
    // System.out.println(fileUrl);
    // }
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
  }

  /**
   * <p>
   * </p>
   *
   * @param pluginDescriptor
   */
  public void addPluginDescriptor(URL pluginDescriptorUrl) {
    checkNotNull(pluginDescriptorUrl);

    try {

      //
      Reader in = new BufferedReader(new InputStreamReader(pluginDescriptorUrl.openStream()));
      PluginDescriptor pluginDescriptor = _gson.fromJson(in, PluginDescriptor.class);
      _pluginDescriptors.put(pluginDescriptor.getId(), pluginDescriptor);

      //
      System.out.println(pluginDescriptor);

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
