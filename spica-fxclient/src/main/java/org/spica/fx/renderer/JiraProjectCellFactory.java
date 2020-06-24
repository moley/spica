package org.spica.fx.renderer;

import javafx.scene.control.ListCell;
import org.spica.commons.services.jira.JiraProject;

public class JiraProjectCellFactory extends ListCell<JiraProject> {

  @Override protected void updateItem(JiraProject item, boolean empty) {
    super.updateItem(item, empty);

    if (empty || item == null) {
      setText(null);
      setGraphic(null);
    }
    else {
      setText(item.getKey() + " - " + item.getName());
    }
  }
}