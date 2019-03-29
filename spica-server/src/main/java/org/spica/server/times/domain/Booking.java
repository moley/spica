package org.spica.server.times.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.spica.server.commons.ReferenceType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Booking {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String userId;

  private String userName;

  private LocalDateTime beginning;

  private LocalDateTime end;

  private Long referenceID;

  @Enumerated(EnumType.STRING)
  private ReferenceType referenceType;

  private String type;

}
