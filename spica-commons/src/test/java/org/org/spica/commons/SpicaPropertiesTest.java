package org.org.spica.commons;

import com.google.common.io.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import org.junit.After;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.spica.commons.KeyValue;
import org.spica.commons.SpicaProperties;

public class SpicaPropertiesTest {



  @After
  public void after () {
    SpicaProperties.close();
  }


  @Test
  public void getKeyValuePairs () {
    SpicaProperties spicaProperties = new SpicaProperties();
    List<KeyValue> keyValuePairs = spicaProperties.getKeyValuePairs("spica.software.deployment");
    Assert.assertFalse ("No deployment enums found (" + spicaProperties.getPropertiesFilesFromFileSystem() + ")", keyValuePairs.isEmpty());
    Assert.assertEquals ("Key invalid", "spica.software.deployment.mobile",  keyValuePairs.get(0).getKey());
    Assert.assertEquals ("Value invalid", "Mobilephone", keyValuePairs.get(0).getValue());
  }

  @Test
  public void save () throws IOException {
    File tempDir = Files.createTempDir();
    SpicaProperties.setSpicaHome(tempDir);
    SpicaProperties spicaProperties = new SpicaProperties();
    Properties properties = new Properties();
    properties.setProperty("custom1", "value1");
    properties.setProperty("custom2", "value2");
    spicaProperties.saveCustomProperties(properties);
    System.out.println (tempDir.getAbsolutePath());
    File customFile = new File (tempDir, "spica-custom.properties");
    Properties propertiesReLoaded = new Properties();
    propertiesReLoaded.load(new FileInputStream(customFile));
    Assert.assertEquals ("value1", propertiesReLoaded.getProperty("custom1"));
    Assert.assertEquals ("value2", propertiesReLoaded.getProperty("custom2"));

  }
}
