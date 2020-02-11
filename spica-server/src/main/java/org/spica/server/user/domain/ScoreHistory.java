package org.spica.server.user.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.spica.server.commons.Idable;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreHistory implements Idable {

  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy = "uuid")
  private String id;

  private String userId;

  private int update;

  private int total;

  private ScoreReason reason;
}
