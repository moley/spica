package org.spica.fx.renderer;

import java.time.LocalDate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.controlsfx.control.PopOver;
import org.spica.fx.Consts;
import org.spica.fx.Reload;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.model.TaskInfo;

public class TaskInfoCellFactory extends ListCell<TaskInfo> {

  private ActionContext actionContext;
  private Reload reload;

  public TaskInfoCellFactory (final ActionContext actionContext, Reload reload) {
    this.actionContext = actionContext;
    this.reload = reload;
  }

  @Override protected void updateItem(TaskInfo item, boolean empty) {
    super.updateItem(item, empty);
    setText(null);
    if (item != null) {
      HBox hbox = new HBox();
      hbox.setAlignment(Pos.CENTER_LEFT);
      Pane panFiller = new Pane();
      HBox.setHgrow(panFiller, Priority.ALWAYS);
      Label lblName = new Label(item.getName());
      DatePicker datePicker = new DatePicker();
      datePicker.setShowWeekNumbers(true);
      datePicker.setValue(item.getPlannedDate());
      datePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
        @Override public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
            LocalDate newValue) {
          item.setPlannedDate(newValue);
          actionContext.saveModel("Set planned date of task " + item.getId() + " to " + newValue);
          reload.reload();

        }
      });
      Button btnFinish = new Button();
      btnFinish.setGraphic(Consts.createIcon("fa-check-circle", Consts.ICON_SIZE_TOOLBAR));
      btnFinish.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent event) {
          item.setState(TaskInfo.StateEnum.FINISHED);
          actionContext.saveModel("Finish task " + item.getId());
          reload.reload();
        }
      });
      hbox.getChildren().addAll(lblName, panFiller, datePicker, btnFinish);
      setGraphic(hbox);
    }
    else
      setGraphic(null);
  }
}
