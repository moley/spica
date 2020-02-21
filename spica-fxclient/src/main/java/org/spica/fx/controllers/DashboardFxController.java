package org.spica.fx.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.DashboardItemType;
import org.spica.fx.Consts;
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

  @FXML private ToggleButton btnFilterMail;

  @FXML private ToggleButton btnFilterEvent;

  @FXML private ToggleButton btnFilterAll;

  private EventDetailsBuilder eventDetailsBuilder = new EventDetailsBuilder();

  private FilteredList<DashboardItemInfo> filteredDashboardItems;

  @FXML public void initialize() {
    btnFilterMail.setGraphic(Consts.createIcon("fa-envelope", Consts.ICONSIZE_MENU));
    btnFilterMail.setSelected(true);
    btnFilterMail.setOnAction(event -> adaptFilter());
    btnFilterEvent.setSelected(true);
    btnFilterEvent.setOnAction(event -> adaptFilter());
    btnFilterEvent.setGraphic(Consts.createIcon("fa-book", Consts.ICONSIZE_MENU));
    btnFilterAll.setText("ALL");
    btnFilterAll.setOnAction(event -> adaptFilter());

  }

  private void adaptFilter() {
    Collection<String> itemTypes = new ArrayList<>();
    if (btnFilterEvent.isSelected())
      itemTypes.add(DashboardItemType.EVENT.name());
    if (btnFilterMail.isSelected())
      itemTypes.add(DashboardItemType.MAIL.name());

    boolean filterAll = btnFilterAll.isSelected();
    LOGGER.info("Filter with types " + itemTypes + " and all " + filterAll);

    filteredDashboardItems = new FilteredList<DashboardItemInfo>(FXCollections.observableList(getActionContext().getModel().getDashboardItemInfos()));
    filteredDashboardItems.setPredicate(new Predicate<DashboardItemInfo>() {
      @Override public boolean test(DashboardItemInfo dashboardItemInfo) {
        boolean typeEquals = itemTypes.contains(dashboardItemInfo.getItemType());
        boolean allEquals = filterAll || (dashboardItemInfo.isOpen() != null && dashboardItemInfo.isOpen());
        return typeEquals && allEquals;
      }
    });
    lviDashboardItems.setItems(filteredDashboardItems);

  }

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
    lviDashboardItems.setItems(filteredDashboardItems);
    LOGGER.info("setTiming finished");
  }

  public void setActionContext(ActionContext actionContext) {
    super.setActionContext(actionContext);
    eventDetailsBuilder.setModel(actionContext.getModel());
    Tooltip defaultToolTip = new Tooltip(
        actionContext.getModel().getCurrentFile().getAbsolutePath() + "\n" + actionContext.getApi().getCurrentServer());
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

    filteredDashboardItems = new FilteredList<DashboardItemInfo>(
        FXCollections.observableList(dashboardItemInfosFromModel));
    lviDashboardItems.setItems(filteredDashboardItems);
    setTiming();
    adaptFilter();
  }
}
