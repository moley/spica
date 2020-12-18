package org.spica.server.software.domain;

import org.spica.server.software.model.SoftwareAggregate;

public interface SoftwarePersistencePort {

  SoftwareAggregate findAll ();

}
