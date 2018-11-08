package org.spica.server.project.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.spica.server.commons.MemberShipType;

import javax.persistence.*;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMember {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @OneToOne
  private Project project;

  private Long memberID;

  @Enumerated(EnumType.STRING)
  private MemberShipType memberShipType;

  private boolean projectLead;


}
