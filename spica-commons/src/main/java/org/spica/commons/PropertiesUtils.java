package org.spica.commons;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertiesUtils {

  /**
   * Converts a {@link Properties} object to {@link String} and you can
   * also provide a description for the output.
   *
   * @param props an input {@link Properties} to be converted to {@link String}
   * @param desc  an input description for the output
   * @return an output String that could easily parse back to {@link Properties} object
   */
  public String convert2String(final Properties props, String desc) {
    log.info("convert " + props.toString() + " to string");
    final StringWriter sw = new StringWriter();
    String propStr;
    try {
      props.store(sw, desc);
      propStr = sw.toString();
    } catch (IOException e) {
      throw new IllegalStateException("Error creating string from props");
    } finally {
      if (sw != null) {
        try {
          sw.close();
        } catch (IOException e) {
          throw new IllegalStateException("Error creating string from props");
        }
      }
    }

    return propStr;
  }


  /**
   * Converts {@link String} to {@link Properties}
   * @param propsStr an {@link String} input that is saved via convert2String method
   * @return a {@link Properties} object
   */
  public Properties convert2Properties(final String propsStr) {
    final Properties props = new Properties();
    final StringReader sr = new StringReader(propsStr);
    try {
      props.load(sr);
    } catch (IOException e) {
      throw new IllegalStateException("Error loading " + propsStr);
    } finally {
      if (sr != null)
        sr.close();
    }

    return props;
  }

  public Properties convert2Properties (final Map<String, String> map) {
    Properties properties = new Properties();
    for (String nextKey: map.keySet()) {
      String value = map.get(nextKey);
      properties.setProperty(nextKey, value);
    }

    return properties;
  }

  public HashMap<String, String> convert2HashMap (final Properties properties) {
    HashMap<String, String> map = new HashMap<>();
    for (String nextKey:properties.stringPropertyNames()) {
      String value = properties.getProperty(nextKey);
      map.put(nextKey, value);
    }

    return map;
  }
}
