package org.spica.commons;

import java.math.BigDecimal;
import java.util.Properties;
import org.junit.jupiter.api.Test;

public class PropertiesUtilsTest {

  private final PropertiesUtils propertiesUtils = new PropertiesUtils();

  @Test
  public void stringToProperties () {

    Properties properties = new Properties();
    properties.put("key1", new BigDecimal("10").toString());

    String asString = propertiesUtils.convert2String(properties, "");
    Properties newProperties = propertiesUtils.convert2Properties(asString);
    System.out.println (newProperties);

  }
}
