package org.spica.cli.actions;

import org.spica.commons.SpicaProperties;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.ModelCacheService;

import java.io.File;

public class StandaloneActionContext implements ActionContext {


    private SpicaProperties spicaProperties = new SpicaProperties();

    @Override
    public File getCurrentWorkingDir() {
        return new File ("").getAbsoluteFile();
    }

    @Override
    public ModelCache getModelCache() {
        ModelCacheService modelCacheService = new ModelCacheService();
        return modelCacheService.get();
    }

    @Override
    public SpicaProperties getSpicaProperties() {
        return spicaProperties;
    }
}
