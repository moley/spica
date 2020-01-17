package org.spica.javaclient.actions.links;

import java.io.File;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.links.LinkFinder;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.LinkInfo;
import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.utils.RenderUtil;

public class ListLinksAction extends AbstractAction {

  private final static Logger LOGGER = LoggerFactory.getLogger(ListLinksAction.class);

  private RenderUtil renderUtil = new RenderUtil();

  @Override public String getDisplayname() {
    return "List links";
  }

  @Override public String getDescription() {
    return "List all links for the current context (--all shows all available)";
  }

  @Override public void execute(ActionContext actionContext, InputParams inputParams,
      CommandLineArguments commandLineArguments) {

    boolean showAllLinks = commandLineArguments.hasArgument("--all");

    EventInfo eventInfo = actionContext.getModelCache().findLastOpenEventFromToday();
    TopicInfo topicInfo = (eventInfo != null && eventInfo.getEventType().equals(EventType.TOPIC)) ?
        actionContext.getModelCache().findTopicInfoById(eventInfo.getReferenceId()) :
        null;
    File currentPath = new File("").getAbsoluteFile();

    if (showAllLinks)
      outputDefault("Show all links:");
    else {
      outputDefault("Show links for:");
      outputDefault("- Topic " + renderUtil.getTopic(topicInfo));
      outputDefault("- Path  " + currentPath.getAbsolutePath());
    }

    outputDefault("");

    LinkFinder linkFinder = new LinkFinder();
    linkFinder.setModelCache(actionContext.getModelCache());
    List<LinkInfo> linkInfos = actionContext.getModelCache().getLinkInfos();
    if (!showAllLinks)
      linkInfos = linkFinder.findMatchingLinks(topicInfo, currentPath);

    for (LinkInfo next : linkInfos) {
      outputDefault(renderUtil.getLink(next));
    }
  }

  @Override public ActionGroup getGroup() {
    return ActionGroup.LINKS;
  }

  @Override public Command getCommand() {
    return new Command("list", "l");
  }
}
