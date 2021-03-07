package org.spica.server.software.domain;

import java.util.ArrayList;
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

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Software extends ObjectWithStatus {

  @Id
  @EqualsAndHashCode.Include
  private String id;

  private String name;
  @Column(length = 10000)
  private String description;

  @ElementCollection
  private Set<String> contacts = new HashSet<>();


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

  @OneToMany(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE})
  private List<TeamMember> teamMembers;

  private Integer technicalDebt;


  public boolean containsTokens (final String filter) {
    String [] tokens = filter.split(" ");
    for (String next: tokens) {
      if (! toString().contains(next.trim()))
        return false;
    }

    return true;
  }





}
