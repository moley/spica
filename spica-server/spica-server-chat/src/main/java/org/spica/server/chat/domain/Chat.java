package org.spica.server.chat.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Chat {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ElementCollection
  private Collection<String> userIDs = new ArrayList<String>();

  
  
}
