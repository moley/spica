package org.spica.server.project.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.spica.server.commons.MemberShipType;

import javax.persistence.*;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMember {

  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy = "uuid")
  private String id;

  @OneToOne
  private Project project;

  private String memberID;

  @Enumerated(EnumType.STRING)
  private MemberShipType memberShipType;

  private boolean projectLead;


}
