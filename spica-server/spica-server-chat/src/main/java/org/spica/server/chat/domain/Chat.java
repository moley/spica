package org.spica.server.chat.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Chat {

  @Id
  //TODO @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ElementCollection
  private Collection<String> userIDs = new ArrayList<String>();

  
  
}
