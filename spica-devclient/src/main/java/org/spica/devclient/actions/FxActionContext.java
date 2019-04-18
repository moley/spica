package org.spica.devclient.actions;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.spica.commons.SpicaProperties;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.ModelCacheService;
import org.spica.javaclient.model.TopicInfo;

import java.io.File;
import java.util.HashMap;

public class FxActionContext implements ActionContext {

    private SpicaProperties spicaProperties = new SpicaProperties();

    private ModelCacheService modelCacheService = new ModelCacheService();


    private HashMap<Class, StringProperty> buttonHashMap = new HashMap<Class, StringProperty>();

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
    public void adaptButtonText(Class action, String buttontext) {
        StringProperty textProperty = buttonHashMap.get(action);
        textProperty.set(buttontext);
    }

    public void registerButtonText (Class action, StringProperty simpleStringProperty) {
        this.buttonHashMap.put(action, simpleStringProperty);
    }


}
