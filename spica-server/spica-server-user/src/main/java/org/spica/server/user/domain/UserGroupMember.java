package org.spica.server.user.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class UserGroupMember {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne
  private UserGroup userGroup;

  @OneToOne
  private User user;

  private boolean groupLead;


}
