package org.spica.devclient.ui.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.FoundAction;
import org.spica.javaclient.actions.params.InputParam;
import org.spica.javaclient.actions.params.InputParamGroup;
import org.spica.javaclient.actions.params.TextInputParam;

import java.util.List;

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
        List<InputParamGroup> inputParamGroups = action.getInputParams();
        int y = 0;
        for (InputParamGroup nextGroup : inputParamGroups) {
            for (InputParam nextInputParam : nextGroup.getInputParams()) {

                Label label = new Label(nextInputParam.getDisplayname() + ":");
                panContent.add(label, 0, y);

                Node datanode;

                if (nextInputParam instanceof TextInputParam) {
                    datanode = new TextField();
                    ((TextField) datanode).textProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            LOGGER.info("Set " + nextInputParam.getKey() + " to " + newValue + "(" + System.identityHashCode(nextInputParam));
                            ((TextInputParam) nextInputParam).setValue(newValue);
                        }
                    });
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
                action.execute(actionContext, inputParamGroups, foundAction.getParameter());
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
