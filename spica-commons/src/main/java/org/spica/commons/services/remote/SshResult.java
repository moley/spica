package org.spica.commons.services.remote;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SshResult {

  private String hostname;

  private String output;
}
