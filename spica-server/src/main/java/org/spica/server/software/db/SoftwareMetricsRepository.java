package org.spica.server.software.db;

import org.spica.server.software.domain.SoftwareMetrics;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoftwareMetricsRepository extends CrudRepository<SoftwareMetrics, Long> {

}
