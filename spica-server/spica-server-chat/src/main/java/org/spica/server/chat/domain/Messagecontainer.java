package org.spica.server.chat.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.spica.server.commons.ReferenceType;

@Entity
@Data
@NoArgsConstructor
public class Messagecontainer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Message> messages = new ArrayList<Message>();

	private Long referenceID;

	@Enumerated(EnumType.STRING)
	private ReferenceType referenceType;

	
}
