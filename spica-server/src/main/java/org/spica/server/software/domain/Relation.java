package org.spica.server.software.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.spica.server.commons.Idable;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Relation implements Idable {

  @Id
  @EqualsAndHashCode.Include
  private String id;

  @OneToOne
  private Software source;

  @OneToOne
  private Software target;

  private String state;

}
