package org.spica.server.project.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.spica.server.commons.Idable;
import org.spica.server.communication.domain.Message;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project implements Idable {

	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;

	private String name;

	@OneToOne
	private Project parentProject;

	private String visibilityRules;

	private String creatorID;





}
