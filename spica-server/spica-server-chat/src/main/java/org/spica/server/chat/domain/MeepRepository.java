package org.spica.server.chat.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeepRepository extends JpaRepository<Meep, Long> {
	
	

}
