package org.spica.server.software.domain;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
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

  private LocalDate creationdate;

  @Column (length = 10000)
  private String metrics;

  private int totalNumber; //number of software entities


}
