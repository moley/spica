package org.spica.server.chat.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.spica.server.commons.ReferenceType;

@Entity
@Data
@NoArgsConstructor
public class MeepContainer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Meep> meeps = new ArrayList<Meep>();

	private Long referenceID;

	@Enumerated(EnumType.STRING)
	private ReferenceType referenceType;

	
}
