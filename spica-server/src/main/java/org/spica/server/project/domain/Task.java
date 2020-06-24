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
public class Task implements Idable {

	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;

	private String name;

	@Column(length = 100000)
	private String description;

	private String projectID;

	private String visibilityRules;

	private String creatorID;

	private String currentUserID;

	@OneToOne
	private Task parentTask;

	/**
	 * the topic key in the external system
	 */
	private String externalSystemKey;

	/**
	 * the ID of the external System (e.g. JIRA)
	 */
	private String externalSystemID;





}
