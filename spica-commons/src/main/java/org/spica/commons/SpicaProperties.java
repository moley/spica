package org.spica.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j public class SpicaProperties {

  private static Properties properties = new Properties();

  private static Properties customPropertiesFromFile = new Properties();

  public final static String FILENAME_SPICA_PROPERTIES = "spica.properties";
  public final static String FILENAME_SPICA_CUSTOM_PROPERTIES = "spica-custom.properties";

  private static File customSpicaPath;

  public SpicaProperties() {
    if (properties.isEmpty()) {
      try {
        //properties from classpath
        Enumeration<URL> resources = getClass().getClassLoader().getResources(FILENAME_SPICA_PROPERTIES);

        while (resources.hasMoreElements()) {
          URL next = resources.nextElement();
          properties.load(next.openStream());
          logProperties(properties, "property file " + next.toString());
        }

        logProperties(properties, "defaults");

        Collection<File> customPropertiesFiles = getPropertiesFilesFromFileSystem();
        for (File next : customPropertiesFiles) {
          Properties customProperties = new Properties();
          customProperties.load(new FileInputStream(next));
          properties.putAll(customProperties);
          customPropertiesFromFile.putAll(customProperties);
          logProperties(properties, "property file " + next.getAbsolutePath());
        }

        properties.putAll(System.getProperties());
        logProperties(properties, "systemproperties");

      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    }
  }

  public Collection<File> getPropertiesFilesFromFileSystem() {
    List<File> files = new ArrayList<>();

    //global default spica properties
    File globalPropertiesFile = new File(getGlobalSpicaHome(), FILENAME_SPICA_PROPERTIES).getAbsoluteFile();
    if (globalPropertiesFile.exists())
      files.add(globalPropertiesFile);

    //local default configs overwrite global configs
    File localPropertiesFile = new File(getSpicaHome(), FILENAME_SPICA_PROPERTIES).getAbsoluteFile();
    if (localPropertiesFile.exists())
      files.add(localPropertiesFile);

    //local custom configs overwrite local default configs
    File globalCustomPropertiesFile = getCustomPropertiesFile().getAbsoluteFile();
    if (globalCustomPropertiesFile.exists())
      files.add(globalCustomPropertiesFile);

    return files;
  }

  public void saveCustomProperties(final Properties saveProperties) {
    for (String key : saveProperties.stringPropertyNames()) {
      String value = saveProperties.getProperty(key);
      setProperty(key, value);
    }

    File globalCustomPropertiesFile = getCustomPropertiesFile().getAbsoluteFile();
    globalCustomPropertiesFile.getParentFile().mkdirs();
    try {
      if (!globalCustomPropertiesFile.exists())
        globalCustomPropertiesFile.createNewFile();

      Properties properties = new Properties();
      properties.load(new FileInputStream(globalCustomPropertiesFile));
      properties.putAll(saveProperties);
      properties.store(new FileOutputStream(globalCustomPropertiesFile), "Saved from spica, do not change");
    } catch (IOException e) {
      throw new IllegalStateException(
          "Cannot save properties " + saveProperties + " in " + globalCustomPropertiesFile.getAbsolutePath() + e
              .getLocalizedMessage(), e);
    }

  }

  public File getCustomPropertiesFile() {
    return new File(getSpicaHome(), FILENAME_SPICA_CUSTOM_PROPERTIES).getAbsoluteFile();
  }

  public Properties getCustomProperties() {
    File globalCustomPropertiesFile = getCustomPropertiesFile();
    if (!globalCustomPropertiesFile.exists())
      return new Properties();

    try {
      Properties properties = new Properties();
      properties.load(new FileInputStream(globalCustomPropertiesFile));
      return properties;
    } catch (IOException e) {
      throw new IllegalStateException(
          "Cannot get custom properties in " + globalCustomPropertiesFile.getAbsolutePath() + "-" + e
              .getLocalizedMessage(), e);
    }
  }

  public void setProperty(final String key, String value) {
    if (value == null)
      properties.remove(key);
    else
      properties.setProperty(key, value);
  }

  public Properties getProperties() {
    return properties;
  }

  private void logProperties(final Properties properties, final String state) {
    log.info("Properties after loading " + state);
    for (Object nextKey : new ArrayList(properties.keySet())) {
      String nextKeyAsString = (String) nextKey;
      String nextValue = properties.getProperty(nextKeyAsString);
      log.info("- " + nextKey + "=" + nextValue);
    }
  }

  public Integer getValueAsInt(final String asString) {
    if (asString == null || asString.trim().isEmpty())
      return 0;
    else
      return new Integer(asString);
  }

  public boolean getValueAsBoolean(final String key) {
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

  public String getValueNotNull(final String key) {
    String value = getValue(key);
    if (value == null)
      throw new IllegalStateException("Property " + key + " is null,but must be set");
    return value;
  }

  public String getValueOrDefault(final String key, final String defaultValue) {
    String configuredValue = getValue(key);
    return configuredValue != null ? configuredValue : defaultValue;
  }

  public String getValue(final String key) {
    Collection<String> values = getValues(key);
    if (values.size() == 0)
      return null;
    else if (values.size() > 1)
      throw new IllegalStateException("Not 1 value for key " + key + " found, but " + values.size());

    return values.iterator().next();
  }

  public List<KeyValue> getKeyValuePairs(final String prefix) {
    List<KeyValue> values = new ArrayList<KeyValue>();
    for (Object nextKey : new ArrayList(properties.keySet())) {
      String nextKeyAsString = (String) nextKey;
      try {
        if (nextKeyAsString.equals(prefix) || nextKeyAsString.startsWith(prefix + ".")) {
          String nextValue = properties.getProperty(nextKeyAsString);
          values.add(new KeyValue(nextKeyAsString, nextValue));
        }
      } catch (Exception e) {
        log.error("Error reading key " + nextKeyAsString + ":" + e.getLocalizedMessage(), e);
      }
    }

    return values;

  }

  public KeyValue getKeyValuePair(final String prefix) {
    if (prefix == null)
      return null;
    List<KeyValue> values = getKeyValuePairs(prefix);
    if (values.size() != 1) {
      log.info("Found values:"+ values);
      throw new IllegalStateException("Not exactly one value found for value '" + prefix + "', but " + values.size());
    }

    return values.get(0);
  }

  public List<String> getValues(final String prefix) {
    List<String> values = new ArrayList<String>();
    for (Object nextKey : new ArrayList(properties.keySet())) {
      String nextKeyAsString = (String) nextKey;
      if (nextKeyAsString.equals(prefix) || nextKeyAsString.startsWith(prefix + ".")) {
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

      File local = new File(".spica").getAbsoluteFile();
      if (local.exists())
        return local;

      File parent = new File(new File("").getAbsoluteFile().getParentFile(), ".spica").getAbsoluteFile();
      if (parent.exists())
        return parent;

      return getGlobalSpicaHome();
    }
  }

  public final static File getGlobalSpicaHome() {
    File home = new File(System.getProperty("user.home"));
    return new File(home, ".spica");
  }

  public final static File getImportFolder() {
    File importFolder = new File(getGlobalSpicaHome(), "import");
    if (!importFolder.exists())
      importFolder.mkdirs();
    return importFolder;
  }

  public static void close() {
    properties.clear();
    customSpicaPath = null;
  }

  public static void setSpicaHome(final File spicaHome) {
    SpicaProperties.customSpicaPath = spicaHome.getAbsoluteFile();
  }

}
