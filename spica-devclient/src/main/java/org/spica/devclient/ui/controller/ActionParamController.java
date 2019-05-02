package org.spica.devclient.ui.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.devclient.actions.widgets.ComboBoxAutoComplete;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.FoundAction;
import org.spica.javaclient.actions.params.*;

public class ActionParamController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ActionParamController.class);

    @FXML
    private GridPane panContent;

    private ActionContext actionContext;

    private Stage stage;

    public void setFoundAction(FoundAction foundAction) {
        LOGGER.info("Use foundaction " + System.identityHashCode(foundAction));
        LOGGER.info("Use action " + System.identityHashCode(foundAction.getAction()));
        Action action = foundAction.getAction();
        InputParams inputParams = action.getInputParams(actionContext);
        int y = 0;
        for (InputParamGroup nextGroup : inputParams.getInputParamGroups()) {
            for (InputParam nextInputParam : nextGroup.getInputParams()) {

                Label label = new Label(nextInputParam.getDisplayname() + ":");
                panContent.add(label, 0, y);

                Node datanode;

                if (nextInputParam instanceof TextInputParam) {
                    TextInputParam textInputParam = (TextInputParam) nextInputParam;
                    if (textInputParam.getNumberOfLines() > 1) {
                        datanode = new TextArea();
                        ((TextArea) datanode).setPrefRowCount(((TextInputParam) nextInputParam).getNumberOfLines());
                    }
                    else
                      datanode = new TextField();
                    ((TextInputControl) datanode).textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            LOGGER.info("Set " + nextInputParam.getKey() + " to " + newValue + "(" + System.identityHashCode(nextInputParam));
                           ((TextInputParam) nextInputParam).setValue(newValue);
                        }
                    });
                } else if (nextInputParam instanceof SearchInputParam) {
                    SearchInputParam searchInputParam = (SearchInputParam) nextInputParam;
                    ComboBox<String> comboBox = new ComboBox<String>(FXCollections.observableArrayList(searchInputParam.getItems()));
                    ComboBoxAutoComplete<String> comboBoxAutoComplete = new ComboBoxAutoComplete<String>(comboBox);
                    comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            searchInputParam.setSelected(newValue);
                        }
                    });
                    datanode = comboBox;
                } else
                    throw new IllegalStateException("Input param of type " + nextInputParam.getClass().getSimpleName() + " not supported");

                panContent.add(datanode, 1, y);

                y++;
                System.out.println("Build " + nextInputParam.getDisplayname() + "-" + nextInputParam.getKey() + "-" + nextInputParam.getValue());
            }
        }

        Button btnDo = new Button("Do");
        panContent.add(btnDo, 0, y);
        btnDo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LOGGER.info("Use action " + System.identityHashCode(action));
                action.execute(actionContext, inputParams, foundAction.getParameter());
                stage.close();

            }
        });
    }

    public ActionContext getActionContext() {
        return actionContext;
    }

    public void setActionContext(ActionContext actionContext) {
        this.actionContext = actionContext;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
