package org.spica.server.project.domain;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.spica.server.commons.Idable;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Topic implements Idable {

	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;

	private String name;

	private String description;

	private String projectID;

	private String visibilityRules;

	private String creatorID;

	private String currentUserID;

	@OneToOne
	private Topic parentTopic;

	private String externalSystemKey;

	private String externalSystemID;





}
