package org.spica.server.project.domain;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.spica.server.commons.Idable;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project implements Idable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	@OneToOne
	private Project parentProject;

	private String visibilityRules;

	private Long creatorID;



}
