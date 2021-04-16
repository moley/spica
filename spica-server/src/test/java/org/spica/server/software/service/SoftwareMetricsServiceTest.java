package org.spica.server.software.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.spica.server.software.domain.Software;
import org.spica.server.software.domain.SoftwareMetrics;
import org.spica.server.software.domain.SoftwareMetricsParam;
import org.spica.server.software.model.SoftwareMetricsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class SoftwareMetricsServiceTest {

  @Autowired
  private SoftwareMetricsService softwareMetricsService;


  @Test
  public void get () {

    Software software1 = new Software();
    software1.setType("type1");
    software1.setDeployment("deployment1");


    Software software2 = new Software();
    software2.setType("type2");
    software2.setDeployment("deployment2");

    Software software3 = new Software();

    LocalDate firstOne = LocalDate.of(2010, 5,10);
    LocalDate secondOne = LocalDate.of(2010, 10,10);

    SoftwareMetricsProvider softwareMetricsProvider = new SoftwareMetricsProvider();
    SoftwareMetrics softwareMetrics1 = softwareMetricsProvider.create(Arrays.asList(software1, software2, software3), secondOne);
    SoftwareMetrics softwareMetrics2 = softwareMetricsProvider.create(Arrays.asList(software1, software2, software3), firstOne);


    softwareMetricsService.setSoftwareMetrics(Arrays.asList(softwareMetrics1, softwareMetrics2));

    SoftwareMetricsParam softwareMetricsParam = new SoftwareMetricsParam();
    SoftwareMetricsInfo metricsInfo = softwareMetricsService.getMetricsInfo(softwareMetricsParam);
    Assert.assertEquals("Not enough datasets defined " + metricsInfo.getHistory().getDatasets(),6 ,metricsInfo.getHistory().getDatasets().size());
    Assert.assertEquals("Long of datacents invalid " + metricsInfo.getHistory().getDatasets(),2 ,metricsInfo.getHistory().getDatasets().get(0).getData().size());
    Assert.assertEquals ("10.05.10", metricsInfo.getHistory().getLabels().get(0));
    Assert.assertEquals ("10.10.10", metricsInfo.getHistory().getLabels().get(1));
  }
}
