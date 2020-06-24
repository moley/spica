package org.spica.fx.renderer;

import javafx.util.StringConverter;
import org.spica.commons.services.jira.JiraProject;

public class JiraProjectStringConverter extends StringConverter<JiraProject> {

  @Override public String toString(JiraProject scopItem) {
    if (scopItem == null){
      return null;
    } else {
      return scopItem.getKey() + " - " + scopItem.getName();
    }
  }

  @Override public JiraProject fromString(String string) {
    return null;
  }
}
