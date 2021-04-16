package org.spica.server.software.db;

import java.util.List;
import org.spica.server.software.domain.SoftwareMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoftwareMetricsRepository extends CrudRepository<SoftwareMetrics, Long> {

  List<SoftwareMetrics> findByOrderByCreationdateAsc ();

}
