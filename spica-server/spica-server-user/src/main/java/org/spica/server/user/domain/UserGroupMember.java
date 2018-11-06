package org.spica.server.user.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class UserGroupMember {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private UserGroup userGroup;

  private User user;

  private boolean groupLead;


}
