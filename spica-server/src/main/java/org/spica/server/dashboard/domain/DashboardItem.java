package org.spica.server.dashboard.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.spica.commons.DashboardItemType;
import org.spica.server.commons.Idable;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardItem implements Idable {

  @Id @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy = "uuid")
  private String id;

  private LocalDateTime created;

  private String referenceID;

  @Enumerated(EnumType.STRING)
  private DashboardItemType referenceType;




}
