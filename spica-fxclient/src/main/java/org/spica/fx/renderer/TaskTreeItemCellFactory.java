package org.spica.fx.renderer;

import java.time.LocalDate;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import org.controlsfx.control.PopOver;
import org.spica.fx.Consts;
import org.spica.fx.Reload;
import org.spica.fx.view.TaskView;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.model.ProjectInfo;
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
        hbox.setSpacing(10);
        Pane panFiller = new Pane();
        HBox.setHgrow(panFiller, Priority.ALWAYS);

        Label lblName = new Label(taskInfo.getName());

        String projectname = taskInfo.getProject() != null ? taskInfo.getProject().getName(): "no project";
        String projectstyle = taskInfo.getProject() != null ? "-fx-background-color: #" + taskInfo.getProject().getColor() + ";-fx-text-fill: white;": null;
        Button btnProjectTag = new Button(projectname);
        //btnProjectTag.getStyleClass().setAll("tag");
        btnProjectTag.setStyle(projectstyle);

        ListView<ProjectInfo> lviProjects = new ListView<ProjectInfo>();
        lviProjects.setCellFactory(cellfactory -> new ProjectCellFactory());
        PopOver popOver = new PopOver(lviProjects);
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        btnProjectTag.setOnMouseClicked(mouseEvent -> {
          //Show PopOver when mouse enters label
          lviProjects.setItems(FXCollections.observableArrayList(actionContext.getModel().getProjectInfos()));
          lviProjects.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                popOver.hide();
                ProjectInfo projectInfo = lviProjects.getSelectionModel().getSelectedItem();
                taskInfo.setProject(projectInfo);
                actionContext.saveModel("Assign project " + projectInfo.getId() + " to task " + taskInfo.getId());
                reload.reload();
            }
          });
          popOver.show(btnProjectTag);
        });

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

        hbox.getChildren().addAll(lblName, panFiller, btnProjectTag , datePicker, btnFinish);
        setGraphic(hbox);
      }

    }
  }
}