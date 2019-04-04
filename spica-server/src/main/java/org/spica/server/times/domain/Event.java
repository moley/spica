package org.spica.server.times.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.spica.server.times.model.EventType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Event {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String userId;

  private String userName;

  private LocalDateTime beginning;

  private LocalDateTime end;

  private Long referenceID;

  @Enumerated(EnumType.STRING)
  private EventType eventType;

  private String type;

}
