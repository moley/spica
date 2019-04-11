package org.spica.devclient.ui.actions;

import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateTopicAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(CreateTopicAction.class);


    @Override
    public Button getButton() {
        return null;
    }

    @Override
    public String getDisplayname() {
        return null;
    }

    @Override
    public void execute(String parameterlist) {
        LOGGER.info("Create topic called with parameter " + parameterlist);

    }

    @Override
    public ActionGroup getGroup() {
        return ActionGroup.TOPIC;
    }

    @Override
    public Command getCommand() {
        return new Command ("create", "c");
    }
}
