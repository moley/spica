package org.spica.server.software.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import org.spica.commons.PropertiesUtils;
import org.spica.server.software.domain.MetricType;
import org.spica.server.software.domain.MetricsExtractor;
import org.spica.server.software.domain.Software;
import org.spica.server.software.domain.SoftwareMetrics;

public class SoftwareMetricsProvider {

  private List<MetricsExtractor> metricsExtractors = new ArrayList<>();

  private PropertiesUtils propertiesUtils = new PropertiesUtils();


  public SoftwareMetricsProvider () {
    metricsExtractors.add(new MetricsExtractor("groupByType", "type", MetricType.COUNT_GROUPED));
    metricsExtractors.add(new MetricsExtractor("groupBySoftwareGroup", "softwaregroup", MetricType.COUNT_GROUPED));
    metricsExtractors.add(new MetricsExtractor("groupByState", "state", MetricType.COUNT_GROUPED));
    metricsExtractors.add(new MetricsExtractor("groupByDeployment", "deployment", MetricType.COUNT_GROUPED));
  }

  public SoftwareMetrics create (final Collection<Software> softwareFlat, final LocalDate localDate) {

    Properties properties = new Properties();
    for (Software next: softwareFlat) {
      for (MetricsExtractor nextExtractor: metricsExtractors) {
        nextExtractor.extract(next);
      }
    }

    for (MetricsExtractor nextExtractor: metricsExtractors) {
      properties.putAll(nextExtractor.getExtractedProperties(softwareFlat.size()));
    }

    SoftwareMetrics softwareMetrics = new SoftwareMetrics();
    softwareMetrics.setId(UUID.randomUUID().toString());
    softwareMetrics.setDate(localDate);
    softwareMetrics.setTotalNumber(softwareFlat.size());
    softwareMetrics.setMetrics(propertiesUtils.convert2String(properties, ""));

    return softwareMetrics;
  }
}
