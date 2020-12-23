package org.spica.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpicaProperties {

  private static Properties properties = new Properties();

  private static Properties customPropertiesFromFile = new Properties();

  public final static String FILENAME_SPICA_PROPERTIES = "spica.properties";

  private static File customSpicaPath;

  public SpicaProperties() {
    if (properties.isEmpty()) {
      try {
        //properties from classpath
        Enumeration<URL> resources = getClass().getClassLoader().getResources(FILENAME_SPICA_PROPERTIES);

        while (resources.hasMoreElements()) {
          URL next = resources.nextElement();
          log.info("Found propertiesindex file in " + next.toString());
          properties.load(next.openStream());
        }

        logProperties(properties, "defaults");

        Collection<File> customPropertiesFiles = getCustomPropertiesFile();
        for (File next: customPropertiesFiles) {
          Properties customProperties = new Properties();
          customProperties.load(new FileInputStream(next));
          properties.putAll(customProperties);
          customPropertiesFromFile.putAll(customProperties);
          logProperties(properties, "custom property file " + next.getAbsolutePath());
        }

        properties.putAll(System.getProperties());
        logProperties(properties, "systemproperties");

      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    }
  }

  public Collection<File> getCustomPropertiesFile () {
    List<File> files = new ArrayList<>();

    File globalPropertiesFile = new File (getGlobalSpicaHome(), FILENAME_SPICA_PROPERTIES).getAbsoluteFile();
    if (globalPropertiesFile.exists())
      files.add(globalPropertiesFile);

    //local configs overwrite global configs
    File localPropertiesFile = new File (".spica", FILENAME_SPICA_PROPERTIES).getAbsoluteFile();
    if (localPropertiesFile.exists())
      files.add(localPropertiesFile);

    return files;
  }

  public void setProperty (final String key, String value) {
    if (value == null)
      properties.remove(key);
    else
      properties.setProperty(key, value);
  }

  public Properties getProperties () {
    return properties;
  }

  private void logProperties (final Properties properties, final String state) {
    log.info("Properties after loading " + state);
    for (Object nextKey : new ArrayList(properties.keySet())) {
      String nextKeyAsString = (String) nextKey;
      String nextValue = properties.getProperty(nextKeyAsString);
      log.info("- " + nextKey + "=" + nextValue);
    }
  }

  public Integer getValueAsInt (final String asString) {
    if (asString == null || asString.trim().isEmpty())
      return 0;
    else
      return new Integer(asString);
  }

  public boolean getValueAsBoolean (final String key) {
    String value = getValue(key);
    if (value == null)
      return false;

    if (value.equalsIgnoreCase(Boolean.TRUE.toString()))
      return true;
    else if (value.equalsIgnoreCase(Boolean.FALSE.toString()))
      return false;
    else
      throw new IllegalStateException("configuration with key " + key + " must be 'true' or 'false'");
  }

  public String getValueNotNull (final String key) {
    String value = getValue(key);
    if (value == null)
      throw new IllegalStateException("Property " + key + " is null,but must be set");
    return value;
  }

  public String getValueOrDefault (final String key, final String defaultValue) {
    String configuredValue = getValue(key);
    return configuredValue != null ? configuredValue: defaultValue;
  }

  public String getValue (final String key) {
    Collection<String> values = getValues(key);
    if (values.size() == 0)
      return null;
    else if (values.size() > 1)
      throw new IllegalStateException("Not 1 value for key " + key + " found, but " + values.size());

    return values.iterator().next();
  }

  public Collection<String> getValues (final String prefix) {
    Collection<String> values = new ArrayList<String>();
    for (Object nextKey : new ArrayList(properties.keySet())) {
      String nextKeyAsString = (String) nextKey;
      if (nextKeyAsString.startsWith(prefix)) {
        String nextValue = properties.getProperty(nextKeyAsString);
        values.add(nextValue);
      }
    }

    return values;

  }

  public final static File getSpicaHome() {
    if (customSpicaPath != null)
      return customSpicaPath;
    else {

      File local = new File (".spica").getAbsoluteFile();
      if (local.exists())
        return local;

      File parent = new File (new File ("").getAbsoluteFile().getParentFile(), ".spica").getAbsoluteFile();
      if (parent.exists())
        return parent;

      return getGlobalSpicaHome();
    }
  }

  public final static File getGlobalSpicaHome () {
    File home = new File (System.getProperty("user.home"));
    return new File(home, ".spica");
  }

  public final static File getImportFolder () {
    File importFolder =  new File (getGlobalSpicaHome(), "import");
    if (! importFolder.exists())
      importFolder.mkdirs();
    return importFolder;
  }

  public static void close() {
    properties.clear();
    customSpicaPath = null;
  }

  public static void setSpicaHome(final File spicaHome) {
    SpicaProperties.customSpicaPath = spicaHome;
  }

}
