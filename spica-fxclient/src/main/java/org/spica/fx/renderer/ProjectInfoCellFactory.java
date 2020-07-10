package org.spica.fx.renderer;

import javafx.scene.control.ListCell;
import org.spica.fx.Consts;
import org.spica.javaclient.model.ProjectInfo;

public class ProjectInfoCellFactory extends ListCell<ProjectInfo> {

  @Override protected void updateItem(ProjectInfo item, boolean empty) {
    super.updateItem(item, empty);

    if (empty || item == null) {
      setText(null);
      setGraphic(null);
    }
    else {
      setText(item.getName());
      if (item.getColor() != null)
        setGraphic(Consts.createIcon("fa-circle", Consts.ICON_SIZE_TOOLBAR, item.getColor()));
      else
        setGraphic(Consts.createIcon("fa-circle", Consts.ICON_SIZE_TOOLBAR, "000000"));
    }
  }
}
