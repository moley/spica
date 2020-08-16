package org.spica.fx.renderer;

import java.time.LocalDate;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import org.controlsfx.control.PopOver;
import org.spica.fx.Consts;
import org.spica.fx.Reload;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.model.TaskInfo;

public class TaskTreeItemCellFactory extends TreeCell<TaskTreeItem> {

  private ActionContext actionContext;
  private Reload reload;

  PseudoClass childPseudoClass = PseudoClass.getPseudoClass("view");
  private Model model;

  public TaskTreeItemCellFactory (final ActionContext actionContext, Reload reload) {
    this.actionContext = actionContext;
    this.model = actionContext.getModel();
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

        ProjectInfo projectInfo = model.findProjectInfoById(taskInfo.getProjectId());
        String projectname = projectInfo != null ? projectInfo.getName(): "no project";
        Button btnProjectTag = new Button(projectname);
        if (projectInfo != null &&  projectInfo.getColor() != null)
          btnProjectTag.setBackground(new Background(new BackgroundFill(Color.web(projectInfo.getColor()), CornerRadii.EMPTY, Insets.EMPTY)));

        ListView<ProjectInfo> lviProjects = new ListView<ProjectInfo>();
        lviProjects.setCellFactory(cellfactory -> new ProjectInfoCellFactory());
        PopOver popOver = new PopOver(lviProjects);
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        btnProjectTag.setOnMouseClicked(mouseEvent -> {
          //Show PopOver when mouse enters label
          lviProjects.setItems(FXCollections.observableArrayList(actionContext.getModel().getProjectInfos()));
          lviProjects.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                popOver.hide();
                ProjectInfo projectInfo = lviProjects.getSelectionModel().getSelectedItem();
                if (projectInfo != null) {
                  taskInfo.setProjectId(projectInfo.getId());
                  actionContext.saveModel("Assign project " + projectInfo.getId() + " to task " + taskInfo.getId());
                  reload.reload();
                }
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
            taskInfo.setTaskState(TaskInfo.TaskStateEnum.FINISHED);
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