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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
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
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;
  @Column(length = 10000)
  private String description;

  @ElementCollection
  private Set<String> contacts = new HashSet<>();

  private String type;

  private String status;

  private String statusAdditionalText;

  private String vcs;

  @ElementCollection
  private Set<String> technologies = new HashSet<>();

  private String format;

  @ManyToOne(fetch = FetchType.EAGER, optional = true)
  @JoinColumn(name="parent_id", referencedColumnName = "id")
  private Software parent;

  private String importedFrom;

  @ElementCollection
  private List<String> deployments = new ArrayList<>();

  private String location;




  public boolean containsTokens (final String filter) {
    String [] tokens = filter.split(" ");
    for (String next: tokens) {
      if (! toString().contains(next.trim()))
        return false;
    }

    return true;
  }





}
