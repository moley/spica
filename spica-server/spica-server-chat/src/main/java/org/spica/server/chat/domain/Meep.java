
package org.spica.server.chat.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Meep {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)	
  private Long id;

  private LocalDateTime creationDate;

  private LocalDateTime updateDate;

  @Column(length=10000)
  private String message;

  private String creatorId;
  
  private String type;

  private String visibilityRules;

  @ElementCollection
  private List<String> documentIds = new ArrayList<String>();

  
  

  
}
