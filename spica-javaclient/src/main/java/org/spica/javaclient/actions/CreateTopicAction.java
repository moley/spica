package org.spica.javaclient.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateTopicAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(CreateTopicAction.class);


    @Override
    public boolean fromButton() {
        return true;
    }

    @Override
    public String getDisplayname() {
        return null;
    }

    @Override
    public void execute(ActionContext actionContext, String parameterlist) {
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
