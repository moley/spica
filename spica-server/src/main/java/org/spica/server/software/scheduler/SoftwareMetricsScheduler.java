package org.spica.server.software.scheduler;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.spica.server.software.db.SoftwareMetricsRepository;
import org.spica.server.software.db.SoftwareRepository;
import org.spica.server.software.domain.SoftwareMetrics;
import org.spica.server.software.service.SoftwareMetricsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SoftwareMetricsScheduler {

  @Autowired
  private SoftwareRepository softwareRepository;

  @Autowired
  private SoftwareMetricsRepository softwareMetricsRepository;

  public SoftwareMetricsScheduler () {
    log.info("Start software metrics scheduler");
  }

  @PostConstruct
  public void onStartup() {
    writeMetrics();
  }



  @Scheduled(cron = "0 0 0 * * *")
  public void onSchedule() {
    writeMetrics();
  }

  public void writeMetrics () {
    log.info("Writing metrics for software started");

    SoftwareMetricsProvider softwareMetricsProvider = new SoftwareMetricsProvider();
    SoftwareMetrics softwareMetrics = softwareMetricsProvider.create(softwareRepository.findByParentIdIsNull());

    softwareMetricsRepository.save(softwareMetrics);


    log.info("Writing metrics for software finished");
  }
}
