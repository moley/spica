package org.spica.server.software.db;

import org.spica.server.software.domain.Software;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SoftwareRepository extends CrudRepository<Software, Long> {


}
