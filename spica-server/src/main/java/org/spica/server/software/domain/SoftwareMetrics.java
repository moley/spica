package org.spica.server.software.domain;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SoftwareMetrics {

  @Id
  @EqualsAndHashCode.Include
  private String id;

  private LocalDate date;

  private String metrics;

  private int totalNumber; //number of software entities


}
