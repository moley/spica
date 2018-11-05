package org.spica.server.project.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Topic {

	@Id
	// TODO @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	private Long projectID;

	
}
