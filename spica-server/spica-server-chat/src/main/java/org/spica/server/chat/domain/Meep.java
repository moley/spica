
package org.spica.server.chat.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Meep {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)	
  private Long id;

  private LocalDateTime creationDate;

  @Column(length=10000)
  private String message;


  private String creatorId;
  
  private String type;

  //@ElementCollection
  //private List<String> fileReferences = new ArrayList<String>();

  
  

  
}
