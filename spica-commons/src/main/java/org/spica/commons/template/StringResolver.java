package org.spica.commons.template;

import java.util.HashMap;

public class StringResolver {

  private HashMap<String, String> resolveTokens = new HashMap<String, String>();

  public void replace (final String field, final String value) {
    resolveTokens.put("#" + field + "#", value);
  }

  public String resolve (final String origin) {
    String replaced = origin;
    for (String next: resolveTokens.keySet()) {
      String value = resolveTokens.get(next);
      replaced = replaced.replace(next, value);
    }

    return replaced;
  }
}
