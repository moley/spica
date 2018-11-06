package org.spica.server.user.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.spica.server.commons.Role;

import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;
  
  private String firstname;
  
  private String username;
  
  private String email;
  
  private String password;
  
  private String token;
  
  private String avatar;

  private LocalDateTime createdAt;

  private boolean active;

  private Role role;

  private Collection<UserGroup> userGroupsCollection;

  
}
