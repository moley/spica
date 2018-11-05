package org.spica.server.chat.domain;

import org.spica.server.commons.ReferenceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeepContainerRepository extends JpaRepository<MeepContainer, Long> {
	
	public MeepContainer findByReferenceTypeAndReferenceID (ReferenceType referenceType, Long referenceID);

}
