package org.org.spica.commons;

import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.spica.commons.KeyValue;
import org.spica.commons.SpicaProperties;

public class SpicaPropertiesTest {

  @Test
  public void getKeyValuePairs () {
    SpicaProperties spicaProperties = new SpicaProperties();
    List<KeyValue> keyValuePairs = spicaProperties.getKeyValuePairs("spica.software.deployment");
    Assert.assertFalse ("No deployment enums found", keyValuePairs.isEmpty());
    Assert.assertEquals ("Key invalid", "mobile",  keyValuePairs.get(0).getKey());
    Assert.assertEquals ("Value invalid", "Mobilephone", keyValuePairs.get(0).getValue());
  }
}
