package org.spica.devclient.actions;

import org.spica.commons.SpicaProperties;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.ModelCacheService;

import java.io.File;

public class FxActionContext implements ActionContext {

    private SpicaProperties spicaProperties = new SpicaProperties();

    private ModelCacheService modelCacheService = new ModelCacheService();


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
    }


    @Override
    public SpicaProperties getSpicaProperties() {
        return spicaProperties;
    }
}
