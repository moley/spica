package org.spica.fx.renderer;

import java.time.LocalDate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TreeCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.spica.fx.Consts;
import org.spica.fx.Reload;
import org.spica.fx.view.TaskView;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.model.TaskInfo;

public class TaskTreeItemCellFactory extends TreeCell<TaskTreeItem> {

  private ActionContext actionContext;
  private Reload reload;

  PseudoClass childPseudoClass = PseudoClass.getPseudoClass("view");

  public TaskTreeItemCellFactory (final ActionContext actionContext, Reload reload) {
    this.actionContext = actionContext;
    this.reload = reload;
  }

  @Override protected void updateItem(TaskTreeItem item, boolean empty) {
    super.updateItem(item, empty);

    if (empty || item == null) {
      pseudoClassStateChanged(childPseudoClass, false);
      setText(null);
      setGraphic(null);
    }
    else {
      if (item.getTaskView() != null) {
        pseudoClassStateChanged(childPseudoClass, true);
        setText(item.getTaskView().getName() + " (" + item.getTaskView().getNumberOfTasks() + ")");
      }
      else if (item.getTaskInfo() != null) {
        pseudoClassStateChanged(childPseudoClass, false);
        TaskInfo taskInfo = item.getTaskInfo();
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        Pane panFiller = new Pane();
        HBox.setHgrow(panFiller, Priority.ALWAYS);
        Label lblName = new Label(taskInfo.getName());
        DatePicker datePicker = new DatePicker();
        datePicker.setShowWeekNumbers(true);
        datePicker.setValue(taskInfo.getPlannedDate());
        datePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
          @Override public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
              LocalDate newValue) {
            taskInfo.setPlannedDate(newValue);
            actionContext.saveModel("Set planned date of task " + taskInfo.getId() + " to " + newValue);
            reload.reload();

          }
        });
        Button btnFinish = new Button();
        btnFinish.setGraphic(Consts.createIcon("fa-check-circle", Consts.ICON_SIZE_TOOLBAR));
        btnFinish.setOnAction(new EventHandler<ActionEvent>() {
          @Override public void handle(ActionEvent event) {
            taskInfo.setState(TaskInfo.StateEnum.FINISHED);
            actionContext.saveModel("Finish task " + taskInfo.getId());
            reload.reload();
          }
        });
        hbox.getChildren().addAll(lblName, panFiller, datePicker, btnFinish);
        setGraphic(hbox);
      }

    }
  }
}