package org.spica.server.times.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.spica.server.commons.Idable;
import org.spica.server.times.model.EventType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Event implements Idable {

  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy = "uuid")
  private String id;

  private String userId;

  private String userName;

  private LocalDateTime beginning;

  private LocalDateTime finish;

  private Long referenceID;

  @Enumerated(EnumType.STRING)
  private EventType eventType;

  private String type;

}
