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
public class Topic implements Idable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	private String description;

	private Long projectID;

	private String visibilityRules;

	private Long creatorID;

	private Long currentUserID;

	@OneToOne
	private Topic parentTopic;

	private String externalSystemKey;

	private String externalSystemID;





}
