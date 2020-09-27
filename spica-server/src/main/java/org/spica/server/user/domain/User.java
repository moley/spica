package org.spica.server.user.domain;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.spica.server.commons.Idable;
import org.spica.server.commons.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="app_user")
public class User implements Idable, UserDetails {

  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy = "uuid")
  private String id;

  private String name;

  private String nummer;

  private int currentScore;

  private String skills;
  
  private String firstname;
  
  private String username;

  private String displayname;
  
  private String email;
  
  private String password;

  private String avatar;

  private LocalDateTime createdAt;

  private Role role;

  private boolean accountNonExpired;

  private boolean accountNonLocked;

  private boolean credentialsNonExpired;

  private boolean enabled;

  private String phone;

  @Convert(converter = AuthoritiesConverter.class)
  private List<GrantedAuthority> authorities;



}
