package org.spica.fx.controllers;

import java.time.LocalDateTime;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.event.EventDetails;
import org.spica.javaclient.event.EventDetailsBuilder;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.utils.DateUtil;
import org.spica.javaclient.utils.RenderUtil;

public class DashboardController extends AbstractController {

  @FXML private Label lblUsersHeader;

  @FXML
  private Pane panHeader;

  private DateUtil dateUtil = new DateUtil();

  @FXML private Label lblCurrentTime;

  @FXML private Label lblWorkingSince;

  @FXML private Label lblWorkTime;

  @FXML private Label lblPauseTime;

  @FXML private Label lblCurrentTask;


  public void setActionContext(ActionContext actionContext) {
    super.setActionContext(actionContext);
    EventInfo firstTaskOfDay = !actionContext.getModel().getEventInfosRealToday().isEmpty() ?
        actionContext.getModel().getEventInfosRealToday().get(0) :
        null;

    EventDetailsBuilder eventDetailsBuilder = new EventDetailsBuilder();
    eventDetailsBuilder.setModel(actionContext.getModel());
    EventDetails eventDetails = eventDetailsBuilder.getDurationDetails();

    String since = "";
    String task = "";
    EventInfo eventInfo = actionContext.getModel().findLastOpenEventFromToday();
    if (eventInfo != null) {
      if (eventInfo.getEventType().equals(EventType.PAUSE)) {
        task = "Pause";
      } else if (eventInfo.getEventType().equals(EventType.TOPIC)) {
        TopicInfo topicInfoById = actionContext.getModel().findTopicInfoById(eventInfo.getReferenceId());
        RenderUtil renderUtil = new RenderUtil();
        task = "Topic " + renderUtil.getTopic(topicInfoById);
      } else if (eventInfo.getEventType().equals(EventType.MESSAGE)) {
        task = eventInfo.getName();
      } else {
        task = eventInfo.getEventType().getValue();
      }

      since = dateUtil.getTimeAsString(eventInfo.getStart());
    }

    lblCurrentTime.setText(dateUtil.getTimeAsString(LocalDateTime.now()));
    lblWorkingSince.setText(firstTaskOfDay != null ? dateUtil.getTimeAsString(firstTaskOfDay.getStart()) : "");
    lblWorkTime.setText(dateUtil.getDuration(eventDetails.getDurationWork()));
    lblPauseTime.setText(dateUtil.getDuration(eventDetails.getDurationPause()));
    lblCurrentTask.setText(task);
    lblWorkingSince.setText(since);
  }
}
