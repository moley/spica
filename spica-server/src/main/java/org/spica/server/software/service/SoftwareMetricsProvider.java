package org.spica.server.software.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class SoftwareMetricsProvider {

  private List<MetricsExtractor> metricsExtractors = new ArrayList<>();


  public SoftwareMetricsProvider () {
    metricsExtractors.add(new MetricsExtractor("typegroup", "type", MetricType.COUNT_GROUPED));

  }

  SoftwareMetrics create (final List<Software> softwareFlat) {

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
    softwareMetrics.setDate(LocalDate.now());
    softwareMetrics.setTotalNumber(softwareFlat.size());
    softwareMetrics.setMetrics(properties.toString());

    return softwareMetrics;
  }
}
