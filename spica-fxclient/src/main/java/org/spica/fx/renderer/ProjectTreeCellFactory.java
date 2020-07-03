package org.spica.fx.renderer;

import javafx.scene.control.TreeCell;
import org.spica.fx.Consts;
import org.spica.fx.Reload;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.model.ProjectInfo;

public class ProjectTreeCellFactory extends TreeCell<ProjectInfo> {

  private ActionContext actionContext;
  private Reload reload;

  public ProjectTreeCellFactory(final ActionContext actionContext, Reload reload) {
    this.actionContext = actionContext;
    this.reload = reload;
  }

  @Override protected void updateItem(ProjectInfo item, boolean empty) {
    super.updateItem(item, empty);

    if (empty || item == null) {
      setText(null);
      setGraphic(null);
    }
    else {
      setText(item.getName() != null ? item.getName(): "Projects");
      if (item.getColor() != null)
        setGraphic(Consts.createIcon("fa-circle", Consts.ICON_SIZE_TOOLBAR, item.getColor()));
      else
        setGraphic(Consts.createIcon("fa-circle", Consts.ICON_SIZE_TOOLBAR, "000000"));
    }
  }
}