package org.spica.server.software.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.spica.server.commons.Idable;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Software implements Idable {

  @Id
  @EqualsAndHashCode.Include
  private String id;

  private String name;
  @Column(length = 10000)
  private String description;

  private String type;

  private String softwaregroup;

  private String state;

  private String stateAdditionalText;

  private String vcs;

  @ElementCollection
  private List<String> technologies = new ArrayList<>();

  private String format;

  private String parentId;

  @OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE})
  private List<Software> children;

  private String importedFrom;

  private String deployment;

  private String location;

  private Boolean active;

  private Integer complexity;

  private Integer maintainability;

  private Boolean needsAction;

  private String needsActionDescription;

  private String requirement;

  private Boolean fitsArchitecture;

  private String architectureExceptions;


  @OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE})
  private List<Contact> contactsUser;

  @OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE})
  private List<Contact> contactsTeam;

  private Integer technicalDebt;

  private Integer changeFrequency;

  private String buildsystem;

  private String bugtracking;

  private LocalDate targetDate;

  private Boolean withOnlineHelp;

  private Boolean withMonitoring;

  private Boolean withPersistence;

  private Boolean withSecurity;

  private Boolean withUi;

  private String deploymentName;


  public boolean containsTokens (final String filter) {
    String [] tokens = filter.split(" ");
    for (String next: tokens) {
      if (! toString().contains(next.trim()))
        return false;
    }

    return true;
  }





}
