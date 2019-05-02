package org.spica.devclient.actions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import org.spica.commons.SpicaProperties;
import org.spica.devclient.util.UiUtils;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.ModelCacheService;
import org.spica.javaclient.model.TopicInfo;

import java.io.File;
import java.util.HashMap;

public class FxActionContext implements ActionContext {

    private SpicaProperties spicaProperties = new SpicaProperties();

    private ModelCacheService modelCacheService = new ModelCacheService();


    private HashMap<Class, Button> buttonHashMap = new HashMap<Class, Button>();

    private ObservableList<TopicInfo> topicInfoObservableList = FXCollections.observableArrayList();



    public void refreshTopicInfoObservableList () {
        topicInfoObservableList.setAll(getModelCache().getTopicInfos());
    }

    public ObservableList<TopicInfo> getTopicInfoObservableList () {
        return topicInfoObservableList;
    }


    @Override
    public File getCurrentWorkingDir() {
        return null;
    }

    @Override
    public ModelCache getModelCache() {
        return modelCacheService.get();
    }

    @Override
    public ModelCacheService getModelCacheService() {
        return modelCacheService;
    }

    @Override
    public void saveModelCache() {
        modelCacheService.set(modelCacheService.get());
        refreshTopicInfoObservableList();
    }


    @Override
    public SpicaProperties getSpicaProperties() {
        return spicaProperties;
    }

    @Override
    public void adaptButton(Class action, String buttontext, String icon) {
        Button btn = buttonHashMap.get(action);
        btn.tooltipProperty().set(new Tooltip(buttontext));
        btn.setGraphic(UiUtils.getIcon(icon));
    }

    public void registerButton(Class action, Button btn) {
        this.buttonHashMap.put(action, btn);
    }


}
