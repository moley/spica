package org.spica.fx.controllers;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.event.EventDetails;
import org.spica.javaclient.event.EventDetailsBuilder;
import org.spica.javaclient.model.DashboardItemInfo;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.utils.DateUtil;
import org.spica.javaclient.utils.RenderUtil;

public class DashboardFxController extends AbstractFxController {

  private final static Logger LOGGER = LoggerFactory.getLogger(DashboardFxController.class);

  private DateUtil dateUtil = new DateUtil();

  @FXML private Label lblCurrentTime;

  @FXML private Label lblWorkingSince;

  @FXML private Label lblWorkTime;

  @FXML private Label lblPauseTime;

  @FXML private Label lblCurrentTask;

  @FXML private ListView<DashboardItemInfo> lviDashboardItems;

  private ObservableList<DashboardItemInfo> dashboardItemInfos = FXCollections.observableArrayList();


  private EventDetailsBuilder eventDetailsBuilder = new EventDetailsBuilder();

  public void setTiming() {
    LOGGER.info("setTiming");
    ActionContext actionContext = getActionContext();
    EventInfo firstTaskOfDay = !actionContext.getModel().getEventInfosRealToday().isEmpty() ?
        actionContext.getModel().getEventInfosRealToday().get(0) :
        null;

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

    lviDashboardItems.setCellFactory(new Callback<ListView<DashboardItemInfo>, ListCell<DashboardItemInfo>>() {
      @Override public ListCell<DashboardItemInfo> call(ListView<DashboardItemInfo> param) {
        return new DashboardListCell();
      }
    });

    lblCurrentTime.setText(dateUtil.getTimeAsString(LocalDateTime.now()));
    lblWorkingSince.setText(firstTaskOfDay != null ? dateUtil.getTimeAsString(firstTaskOfDay.getStart()) : "");
    lblWorkTime.setText(dateUtil.getDuration(eventDetails.getDurationWork()));
    lblPauseTime.setText(dateUtil.getDuration(eventDetails.getDurationPause()));
    lblCurrentTask.setText(task);
    lblWorkingSince.setText(since);
    lviDashboardItems.setItems(dashboardItemInfos);
    LOGGER.info("setTiming finished");
  }

  public void setActionContext(ActionContext actionContext) {
    super.setActionContext(actionContext);
    eventDetailsBuilder.setModel(actionContext.getModel());
    Tooltip defaultToolTip = new Tooltip(actionContext.getModel().getCurrentFile().getAbsolutePath() + "\n" + actionContext.getApi().getCurrentServer());
    lblCurrentTime.setTooltip(defaultToolTip);
    lblCurrentTask.setTooltip(defaultToolTip);
    lblPauseTime.setTooltip(defaultToolTip);
    lblWorkingSince.setTooltip(defaultToolTip);
    lblWorkTime.setTooltip(defaultToolTip);



    List<DashboardItemInfo> dashboardItemInfosFromModel = actionContext.getModel().getDashboardItemInfos();

    LOGGER.info("Load dashboarditems: " + dashboardItemInfosFromModel.size());

    Collections.sort(dashboardItemInfosFromModel, new Comparator<DashboardItemInfo>() {
      @Override public int compare(DashboardItemInfo o1, DashboardItemInfo o2) {
        return o2.getCreated().compareTo(o1.getCreated());
      }
    });

    dashboardItemInfos.clear();
    dashboardItemInfos.addAll(dashboardItemInfosFromModel);

    setTiming();

  }
}
