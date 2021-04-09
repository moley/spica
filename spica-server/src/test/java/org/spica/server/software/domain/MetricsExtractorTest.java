package org.spica.server.software.domain;

import java.time.LocalDate;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class MetricsExtractorTest {

  @Test
  public void extractSetCountNotNull () {

    Software software1 = new Software();
    software1.setTargetDate(LocalDate.now());

    Software software2 = new Software();

    MetricsExtractor metricsExtractor = new MetricsExtractor("targetDateNotNull", "targetDate", MetricType.COUNT_NOT_NULL);

    metricsExtractor.extract(software1);
    metricsExtractor.extract(software2);

    Assert.assertEquals ("1", metricsExtractor.getExtractedProperties(3).get("targetDateNotNull").toString());
  }


  @Test
  public void extractSetSum () {

    Software software1 = new Software();
    software1.setComplexity(5);

    Software software2 = new Software();
    software2.setComplexity(10);
    Software software3 = new Software();

    MetricsExtractor metricsExtractor = new MetricsExtractor("sumComplexity", "complexity", MetricType.SUM);

    metricsExtractor.extract(software1);
    metricsExtractor.extract(software2);
    metricsExtractor.extract(software3);

    Assert.assertEquals ("15", metricsExtractor.getExtractedProperties(3).get("sumComplexity").toString());



  }

  @Test
  public void extractSetAverage () {

    Software software1 = new Software();
    software1.setComplexity(5);

    Software software2 = new Software();
    software2.setComplexity(8);
    Software software3 = new Software();

    MetricsExtractor metricsExtractor = new MetricsExtractor("sumComplexity", "complexity", MetricType.AVERAGE);

    metricsExtractor.extract(software1);
    metricsExtractor.extract(software2);
    metricsExtractor.extract(software3);

    Assert.assertEquals ("4.33", metricsExtractor.getExtractedProperties(3).get("sumComplexity").toString());



  }

  @Test
  public void extractGroup (){
    Software software1 = new Software();
    software1.setType("type1");

    Software software2 = new Software();
    software2.setType("type2");

    Software software3 = new Software();

    MetricsExtractor metricsExtractor = new MetricsExtractor("countGroupType", "type", MetricType.COUNT_GROUPED);

    metricsExtractor.extract(software1);
    metricsExtractor.extract(software2);
    metricsExtractor.extract(software3);

    Assert.assertEquals ("2", metricsExtractor.getExtractedProperties(3).get("countGroupType").toString());
    Assert.assertEquals ("1", metricsExtractor.getExtractedProperties(3).get("countGroupType.type1").toString());
    Assert.assertEquals ("1", metricsExtractor.getExtractedProperties(3).get("countGroupType.type2").toString());


  }


}
