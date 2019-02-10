package org.spica.server.chat.domain;

import org.spica.server.commons.ReferenceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagecontainerRepository extends JpaRepository<Messagecontainer, Long> {
	
	public Messagecontainer findByReferenceTypeAndReferenceID (ReferenceType referenceType, Long referenceID);

}
