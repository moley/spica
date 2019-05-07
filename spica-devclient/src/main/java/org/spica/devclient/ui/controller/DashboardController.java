package org.spica.devclient.ui.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import org.spica.commons.SpicaProperties;
import org.spica.devclient.actions.FxActionContext;
import org.spica.javaclient.actions.ActionHandler;
import org.spica.javaclient.actions.FoundAction;
import org.spica.javaclient.actions.GotoJiraAction;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.ModelCacheService;
import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.timetracker.TimetrackerService;

public class DashboardController {


    @FXML
    private ListView<TopicInfo> lviTopics;

    SpicaProperties spicaProperties;

    private FxActionContext actionContext = new FxActionContext();

    private ActionHandler actionHandler = new ActionHandler();

    private TimetrackerService timetrackerService = new TimetrackerService();



    public void refreshTimer (ModelCache modelCache) {

        EventInfo openInfo = modelCache.findLastOpenEventFromToday();


        if (openInfo != null && openInfo.getReferenceId() != null) {
            TopicInfo topicInfo = modelCache.findTopicInfoById(openInfo.getReferenceId());
            lviTopics.getSelectionModel().select(topicInfo);
        }

    }


    @FXML
    void initialize() {
        lviTopics.setCellFactory(new Callback<ListView<TopicInfo>, ListCell<TopicInfo>>() {
      @Override
      public ListCell<TopicInfo> call(ListView<TopicInfo> studentListView) {
        return new ListCell<TopicInfo>() {
          @Override
          protected void updateItem(TopicInfo topicInfo, boolean empty) {
            super.updateItem(topicInfo, empty);
            if (empty || topicInfo == null) {
              setText(null);
              setGraphic(null);
            } else {
              if (topicInfo.getExternalSystemKey() != null)
                setText(topicInfo.getExternalSystemKey() + ": " + topicInfo.getName());
              else
                setText(topicInfo.getName());
            }

          }
        };
      }
    });
    actionContext.refreshTopicInfoObservableList();
    lviTopics.setItems(actionContext.getTopicInfoObservableList());

        ModelCacheService modelCacheService = new ModelCacheService();
        ModelCache modelCache = modelCacheService.get();
        timetrackerService.setModelCacheService(modelCacheService);


        lviTopics.setOnMouseReleased(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        // Your action here

        if (event.getClickCount() == 2) {
          FoundAction gotoJiraAction = actionHandler.findAction(GotoJiraAction.class);
          gotoJiraAction.getAction().execute(actionContext, new InputParams(),"");
        }
        else if (event.getClickCount() == 1) {

          TopicInfo newValue = lviTopics.getSelectionModel().getSelectedItem();

          System.out.println("Selected item: " + newValue.getId() + "-" + newValue.getName());
          timetrackerService.startWorkOnTopic(newValue);

          refreshTimer(modelCache);
        }

      }
    });

    }

}
