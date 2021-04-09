package org.spica.server.software.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.spica.server.software.domain.Software;
import org.spica.server.software.domain.SoftwareMetrics;

public class SoftwareMetricsProviderTest {

  @Test
  public void metricsProvider () throws IOException {

    Software software1 = new Software();
    software1.setType("type1");
    software1.setDeployment("deployment1");


    Software software2 = new Software();
    software2.setType("type2");
    software2.setDeployment("deployment2");

    Software software3 = new Software();

    SoftwareMetricsProvider softwareMetricsProvider = new SoftwareMetricsProvider();
    SoftwareMetrics softwareMetrics = softwareMetricsProvider.create(Arrays.asList(software1, software2, software3), LocalDate.now());
    Assert.assertEquals (3, softwareMetrics.getTotalNumber());
    Assert.assertEquals(LocalDate.now(), softwareMetrics.getDate());

    String metrics = softwareMetrics.getMetrics();

    Assert.assertTrue (metrics.contains("groupByType=2"));
    Assert.assertTrue (metrics.contains("groupByType.type2=1"));
    Assert.assertTrue (metrics.contains("groupByType.type1=1"));

    Assert.assertTrue (metrics.contains("groupByDeployment=2"));
    Assert.assertTrue (metrics.contains("groupByDeployment.deployment1=1"));
    Assert.assertTrue (metrics.contains("groupByDeployment.deployment2=1"));
  }
}
