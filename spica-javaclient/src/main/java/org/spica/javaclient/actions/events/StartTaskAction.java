package org.spica.javaclient.actions.events;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.FoundAction;
import org.spica.javaclient.actions.tasks.CreateTaskAction;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.TaskInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.params.Renderer;
import org.spica.javaclient.params.SelectInputParam;
import org.spica.javaclient.events.EventService;
import org.spica.javaclient.utils.RenderUtil;

@Slf4j
public class StartTaskAction extends AbstractAction {

    private final static String SPECIAL_TASK_NEW = "_NEW";

    private RenderUtil renderUtil = new RenderUtil();

    public final static String KEY_TOPIC = "task";

    @Override public String getDisplayname() {
        return "Start task";
    }

    @Override
    public String getDescription() {
        return "Starts working on a task";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        Model model = actionContext.getModel();

        String query = commandLineArguments.getOptionalMainArgument();

        List<TaskInfo> infos = model.findTaskInfosByQuery(query);
        TaskInfo selectedTaskInfo = infos.size() == 1 ? infos.get(0): (TaskInfo) inputParams.getInputValue(KEY_TOPIC);

        String context = "existing";
        if (selectedTaskInfo.getId().equals(SPECIAL_TASK_NEW)) {
            FoundAction foundAction = actionContext.getActionHandler().findAction(CreateTaskAction.class);
            ActionResult actionResult = actionContext.getActionHandler().handleAction(actionContext, foundAction);
            selectedTaskInfo = (TaskInfo) actionResult.getUserObject();
            context = "created";
        }

        EventService eventService = actionContext.getServices().getEventService();
        eventService.startWorkOnTask(selectedTaskInfo);

        outputOk("Start work on " + context + " topic " + renderUtil.getTask(selectedTaskInfo));

        return null;
    }

    @Override
    public ActionGroup getGroup() {
        return ActionGroup.BOOKING;
    }

    @Override
    public InputParams getInputParams(ActionContext actionContext, CommandLineArguments parameterList) {

        InputParams inputParams = new InputParams();
        Model model = actionContext.getModel();
        List<TaskInfo> filteredTaskInfos = model.findTaskInfosByQuery(parameterList.getOptionalMainArgument());
        if (filteredTaskInfos.size() != 1) {
            List<TaskInfo> allTaskInfosForSelection = new ArrayList<>();
            allTaskInfosForSelection.addAll(model.getTaskInfos());
            allTaskInfosForSelection.add(new TaskInfo().name("Create new task").id(SPECIAL_TASK_NEW));

            System.out.println("Did not find exactly one topic with query <" + parameterList + ">");
            SelectInputParam<TaskInfo> topicSearch = new SelectInputParam<TaskInfo>(KEY_TOPIC, "Task: ", allTaskInfosForSelection, new Renderer<TaskInfo>() {
                @Override
                public String toString(TaskInfo topicInfo) {

                    String topicSearch = "";
                    if (topicInfo.getExternalSystemKey() != null)
                        topicSearch += topicInfo.getExternalSystemKey() + " ";

                    topicSearch += topicInfo.getName();
                    return topicSearch;
                }
            });
            InputParamGroup inputParamGroupTask = new InputParamGroup("Task");
            inputParamGroupTask.getInputParams().add(topicSearch);
            inputParams.getInputParamGroups().add(inputParamGroupTask);
        }

        return inputParams;
    }

    @Override
    public Command getCommand() {
        return new Command("task", "t");
    }
}
