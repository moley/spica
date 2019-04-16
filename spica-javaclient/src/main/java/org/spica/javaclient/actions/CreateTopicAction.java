package org.spica.javaclient.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.params.InputParam;
import org.spica.javaclient.actions.params.InputParamGroup;
import org.spica.javaclient.actions.params.TextInputParam;

import java.util.Arrays;
import java.util.List;

public class CreateTopicAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(CreateTopicAction.class);


    @Override
    public boolean fromButton() {
        return true;
    }

    @Override
    public String getDisplayname() {
        return "Create topic";
    }

    @Override
    public String getDescription() {
        return "Creates a topic (parameter is topic subject)";
    }

    @Override
    public void execute(ActionContext actionContext, List<InputParamGroup> inputParamGroups, String parameterList) {
        LOGGER.info("Create topic called with parameter:");
        for (InputParamGroup nextGroup: inputParamGroups) {
            for (InputParam nextParam: nextGroup.getInputParams()) {
                LOGGER.info ( "- " + nextParam.getKey() + "-" + nextParam.getDisplayname() + "-" + nextParam.getValue() + "(" + System.identityHashCode(nextParam) + ")");
            }
        }
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.TOPIC;
    }

    @Override
    public Command getCommand() {
        return new Command ("create", "c");
    }

    @Override
    public List<InputParamGroup> getInputParams() {

        TextInputParam project = TextInputParam.builder().key("project").displayname("Project").value("Default").build();

        InputParamGroup inputParamGroup = new InputParamGroup();
        inputParamGroup.getInputParams().add(project);

        return Arrays.asList(inputParamGroup);
    }
}
