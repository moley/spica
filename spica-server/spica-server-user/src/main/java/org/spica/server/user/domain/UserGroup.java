package org.spica.server.user.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.spica.server.commons.Idable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class UserGroup implements Idable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

}
