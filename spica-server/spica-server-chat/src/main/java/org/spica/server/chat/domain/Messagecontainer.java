package org.spica.server.chat.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.spica.server.commons.ReferenceType;

@Entity
@Data
@NoArgsConstructor
public class Messagecontainer {

	@Id @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	private String id;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Message> messages = new ArrayList<Message>();

	private String referenceID;

	@Enumerated(EnumType.STRING)
	private ReferenceType referenceType;

	
}
