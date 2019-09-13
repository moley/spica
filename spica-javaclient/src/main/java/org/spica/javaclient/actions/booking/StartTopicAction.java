package org.spica.javaclient.actions.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.params.*;
import org.spica.javaclient.model.*;
import org.spica.javaclient.timetracker.TimetrackerService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class StartTopicAction implements Action {

        private final static Logger LOGGER = LoggerFactory.getLogger(StartTopicAction.class);

        @Override
        public String getIcon() {
            return "fa-PHONE"; //TODO
        }

        @Override
        public boolean fromButton() {
            return false; //TODO
        }

        @Override
        public String getDisplayname() {
            return "Start topic";
        }

        @Override
        public String getDescription() {
            return "Starts to work on a topic";
        }

        @Override
        public void execute(ActionContext actionContext, InputParams inputParams, String parameterlist) {

            ModelCache modelCache = actionContext.getModelCache();
            List<TopicInfo> infos = modelCache.findTopicInfosByQuery(parameterlist);
            if (infos.size() != 1)
                throw new IllegalStateException("Did not find exactly one, but " + infos.size() + " topics for query " + parameterlist);

            TopicInfo topicInfo = infos.get(0);
            TimetrackerService timetrackerService = new TimetrackerService();
            timetrackerService.setModelCacheService(actionContext.getModelCacheService());
            timetrackerService.startWorkOnTopic(topicInfo);
            actionContext.saveModelCache();

            System.out.println ("Started work on topic " + topicInfo.getExternalSystemKey() + "-" + topicInfo.getName());
        }

        @Override
        public ActionGroup getGroup() {
            return ActionGroup.BOOKING;
        }

        @Override
        public Command getCommand() {
            return new Command("start", "s");
        }
}
