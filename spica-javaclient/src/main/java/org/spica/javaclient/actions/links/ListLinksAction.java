package org.spica.javaclient.actions.links;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.links.LinkFinder;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.LinkInfo;
import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.utils.RenderUtil;

import java.io.File;
import java.util.List;

public class ListLinksAction implements Action {

        private final static Logger LOGGER = LoggerFactory.getLogger(ListLinksAction.class);

        private RenderUtil renderUtil = new RenderUtil();

        @Override
        public String getDisplayname() {
            return "List links";
        }

        @Override
        public String getDescription() {
            return "List all links for the current context";
        }

        @Override
        public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {

            outputDefault("Links for:");

            EventInfo eventInfo = actionContext.getModelCache().findLastOpenEventFromToday();
            TopicInfo topicInfo = eventInfo.getEventType().equals(EventType.TOPIC) ? actionContext.getModelCache().findTopicInfoById(eventInfo.getReferenceId()): null;
            File currentPath = new File("").getAbsoluteFile();

            outputDefault("Topic : " + renderUtil.getTopic(topicInfo));
            outputDefault("Path  : " + currentPath.getAbsolutePath());

            outputDefault("\n\n");

            LinkFinder linkFinder = new LinkFinder();
            linkFinder.setModelCache(actionContext.getModelCache());
            List<LinkInfo> linkInfos = linkFinder.findMatchingLinks(topicInfo, currentPath);
            for (LinkInfo next: linkInfos) {
                String linkToken = String.format("     %-30s%-70s (%s)", next.getName(), next.getUrl(), next.getId());
                outputOk(linkToken);
            }
        }


        @Override
        public ActionGroup getGroup() {
            return ActionGroup.LINKS;
        }

        @Override
        public Command getCommand() {
            return new Command ("list", "l");
        }
}
