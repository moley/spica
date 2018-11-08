package org.spica.server.project.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.spica.server.commons.Idable;

@Entity
@Data
@NoArgsConstructor
public class Topic implements Idable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	private Long projectID;

	private String visibilityRules;

	private Long creatorID;



}
