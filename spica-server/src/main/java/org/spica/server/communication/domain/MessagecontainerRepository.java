package org.spica.server.communication.domain;

import org.spica.server.commons.ReferenceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagecontainerRepository extends JpaRepository<Messagecontainer, String> {
	
	public Messagecontainer findByReferenceTypeAndReferenceID (ReferenceType referenceType, String referenceID);

}
