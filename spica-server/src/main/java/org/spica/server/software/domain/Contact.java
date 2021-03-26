package org.spica.server.software.domain;

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
public class Contact {

  @Id
  @EqualsAndHashCode.Include
  private String id;

  private String contactId;

  private String role;
}
