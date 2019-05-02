
package org.spica.server.communication.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Message {

  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy = "uuid")
  private String id;

  private LocalDateTime creationDate;

  private LocalDateTime updateDate;

  @Column(length=10000)
  private String message;

  private String creatorId;

  private String visibilityRules;

  @ElementCollection
  private List<Long> documentIds = new ArrayList<Long>();

  
  

  
}
