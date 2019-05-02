package org.spica.javaclient.actions;

import org.spica.commons.SpicaProperties;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.ModelCacheService;

import java.io.File;

public interface ActionContext {

    File getCurrentWorkingDir ();

    ModelCache getModelCache ();

    ModelCacheService getModelCacheService ();

    void saveModelCache();

    SpicaProperties getSpicaProperties ();

    void adaptButton(final Class action, String buttontext, String icon);



}
