package org.spica.fx.renderer;

import javafx.util.StringConverter;
import org.spica.javaclient.model.ProjectInfo;

public class ProjectInfoStringConverter extends StringConverter<ProjectInfo> {

  @Override public String toString(ProjectInfo scopItem) {
    if (scopItem == null){
      return null;
    } else {
      return scopItem.getName();
    }
  }

  @Override public ProjectInfo fromString(String string) {
    return null;
  }
}
