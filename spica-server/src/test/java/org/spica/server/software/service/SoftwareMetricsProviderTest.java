package org.spica.server.software.domain;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.spica.server.software.service.SoftwareMetricsProvider;

public class SoftwareMetricsProviderTest {

  @Test
  public void metricsProvider () throws IOException {

    Software software1 = new Software();
    software1.setType("type1");


    Software software2 = new Software();
    software2.setType("type2");

    Software software3 = new Software();

    SoftwareMetricsProvider softwareMetricsProvider = new SoftwareMetricsProvider();
    SoftwareMetrics softwareMetrics = softwareMetricsProvider.create(Arrays.asList(software1, software2, software3));
    Assert.assertEquals (3, softwareMetrics.getTotalNumber());
    Assert.assertEquals(LocalDate.now(), softwareMetrics.getDate());

    String metrics = softwareMetrics.getMetrics();
    Assert.assertTrue (metrics.contains("typegroup.type2=1"));
    Assert.assertTrue (metrics.contains("typegroup.type1=1"));
    Assert.assertTrue (metrics.contains("typegroup=2"));
  }
}
