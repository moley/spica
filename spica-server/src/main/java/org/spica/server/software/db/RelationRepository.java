package org.spica.server.software.db;

import java.util.Collection;
import org.spica.server.software.domain.Relation;
import org.spica.server.software.domain.Software;
import org.springframework.data.repository.CrudRepository;

public interface RelationRepository extends CrudRepository<Relation, Long> {

  Collection<Relation> findAllBySource(final Software software);
}
