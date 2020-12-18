package org.spica.server.software.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SoftwareRepository extends CrudRepository<SoftwareDb, Long> {


}
