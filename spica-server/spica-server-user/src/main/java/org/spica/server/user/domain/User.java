package org.spica.server.user.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {

  @Id
  //@GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;
  
  private String firstname;
  
  private String username;
  
  private String email;
  
  private String password;
  
  private String token;
  
  private String avatar;

  
}
