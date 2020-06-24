package org.spica.javaclient.actions.booking;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.spica.javaclient.timetracker.TimetrackerService;
import org.spica.javaclient.utils.RenderUtil;

public class StartTaskAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(StartTaskAction.class);

    private final static String SPECIAL_TASK_NEW = "_NEW";

    private RenderUtil renderUtil = new RenderUtil();

    public final static String KEY_TOPIC = "topic";

    @Override public String getDisplayname() {
        return "Start topic";
    }

    @Override
    public String getDescription() {
        return "Starts to work on a topic";
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

        TimetrackerService timetrackerService = new TimetrackerService();
        timetrackerService.setModelCacheService(actionContext.getServices().getModelCacheService());
        timetrackerService.startWorkOnTask(selectedTaskInfo);
        actionContext.saveModel(getClass().getName());

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
        return new Command("topic", "t");
    }
}
