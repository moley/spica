package org.spica.server.software.domain;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import lombok.Getter;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.spica.commons.PropertiesUtils;

@Getter
public class MetricsExtractor {

  private HashMap<String, BigDecimal> extractedProperties = new HashMap<String, BigDecimal>();

  private String field;

  private MetricType metricType;

  private boolean average;

  private String id;

  public MetricsExtractor(final String id, final String field, final MetricType metricType) {
    this.id = id;
    this.field = field;
    this.metricType = metricType;
    if (metricType.equals(MetricType.AVERAGE)) {
      this.average = true;
      this.metricType = MetricType.SUM;
    }
  }

  public String toString () {
    return metricType.getDescription().replace("'%s'", field);
  }

  public void extract (final Software software) {

    try {
      String value = BeanUtils.getSimpleProperty(software, field);
      if (value != null) {
        if (metricType.equals(MetricType.COUNT_NOT_NULL)) {
          String key = id;
          BigDecimal bigDecimal = extractedProperties.get(key);
          if (bigDecimal == null)
            bigDecimal = new BigDecimal(0);

          bigDecimal = bigDecimal.add(new BigDecimal(1));
          extractedProperties.put(key, bigDecimal);
        }
        else if (metricType.equals(MetricType.COUNT_GROUPED)) {

          //grouped value
          String key = id + "." + value;
          BigDecimal bigDecimal = extractedProperties.get(key);
          if (bigDecimal == null)
            bigDecimal = new BigDecimal(0);

          bigDecimal = bigDecimal.add(new BigDecimal(1));
          extractedProperties.put(key, bigDecimal);

          //sum
          String keyGlob = id;
          BigDecimal bigDecimalGlob = extractedProperties.get(key);
          if (bigDecimalGlob == null)
            bigDecimalGlob = new BigDecimal(0);

          bigDecimalGlob = bigDecimalGlob.add(new BigDecimal(1));
          extractedProperties.put(keyGlob, bigDecimalGlob);
        }
        else if (metricType.equals(MetricType.SUM)) {
          String key = id;
          BigDecimal bigDecimal = extractedProperties.get(key);
          if (bigDecimal == null)
            bigDecimal = new BigDecimal(0);
          bigDecimal = bigDecimal.add(new BigDecimal(value));
          extractedProperties.put(key, bigDecimal);
        }

      }


    } catch (IllegalAccessException | InvocationTargetException |NoSuchMethodException e) {
      throw new IllegalStateException(e);
    }



  }

  public HashMap<String, String> getExtractedProperties (final int numberOfEntities) {
    if (average) {
      for (String next: extractedProperties.keySet()) {
        BigDecimal value = extractedProperties.get(next);
        value = value.divide(new BigDecimal(numberOfEntities), 2, RoundingMode.DOWN);
        extractedProperties.put(next, value);
      }
    }

    HashMap<String, String> stringHashMap = new HashMap<String, String>();
    for (String next: extractedProperties.keySet()) {
      BigDecimal value = extractedProperties.get(next);
      stringHashMap.put(next, value.toString());
    }



    return stringHashMap;
  }
}
