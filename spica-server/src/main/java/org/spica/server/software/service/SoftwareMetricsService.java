package org.spica.server.software.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.sound.sampled.Line;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.DateUtils;
import org.spica.commons.PropertiesUtils;
import org.spica.server.software.db.SoftwareMetricsRepository;
import org.spica.server.software.domain.SoftwareMetrics;
import org.spica.server.software.domain.SoftwareMetricsParam;
import org.spica.server.software.model.Diagram;
import org.spica.server.software.model.LineDataSet;
import org.spica.server.software.model.SoftwareMetricsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SoftwareMetricsService {

  @Autowired
  private SoftwareMetricsRepository softwareMetricsRepository;

  private DateUtils dateUtils = new DateUtils();

  private PropertiesUtils propertiesUtils = new PropertiesUtils();

  public SoftwareMetricsInfo getMetricsInfo (final SoftwareMetricsParam param) {

    Diagram diagram = new Diagram();

    HashMap<SoftwareMetrics, Properties> propsPerMetrics = new HashMap<>();
    Set<String> availableKeys = new HashSet<>();

    HashMap<String, LineDataSet> lineDataSets = new HashMap<>();

    //get all keys
    for (SoftwareMetrics next: softwareMetricsRepository.findAll()) {
      String metrics = next.getMetrics();
      diagram.addLabelsItem(dateUtils.getDateAsString(next.getDate()));
      Properties properties = propertiesUtils.convert2Properties(metrics);
      propsPerMetrics.put(next, properties);
      availableKeys.addAll(properties.stringPropertyNames());
    }

    //add linedatasets per key
    for (String nextKey: availableKeys) {
      LineDataSet lineDataSet = new LineDataSet();
      lineDataSet.label(nextKey);
      lineDataSets.put(nextKey, lineDataSet);
    }

    //group all properties to datasets
    for (SoftwareMetrics next: softwareMetricsRepository.findAll()) {

      Properties currentProps = propsPerMetrics.get(next);

      for (String nextKey: availableKeys) {
        LineDataSet lineDataSet = lineDataSets.get(nextKey);
        String property = currentProps.getProperty(nextKey);
        if (property == null)
          property = "0";

        lineDataSet.addDataItem(new BigDecimal(property));

      }
    }

    diagram.setDatasets(new ArrayList<>(lineDataSets.values()));


    SoftwareMetricsInfo softwareMetricsInfo = new SoftwareMetricsInfo();
    softwareMetricsInfo.setHistory(diagram);

    if (true)
      throw new IllegalStateException("TODO: Sort be date");

    return softwareMetricsInfo;
  }
}
