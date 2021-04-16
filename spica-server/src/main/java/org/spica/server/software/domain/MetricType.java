package org.spica.server.software.domain;

import lombok.Getter;

@Getter
public enum MetricType {

  COUNT_NOT_NULL ("Count entities were '%s' is set"),
  COUNT_GROUPED ("Group entities by '%s'"),
  AVERAGE ("Average of '%s'"),
  SUM ("Sum of '%s'");

  private MetricType (final String description) {
    this.description = description;
  }

  private String description;

}
