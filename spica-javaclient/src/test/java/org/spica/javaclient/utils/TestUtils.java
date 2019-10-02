package org.spica.javaclient.utils;

import org.mockito.Mockito;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.model.ModelCache;
import org.spica.javaclient.model.ModelCacheService;

import java.io.File;

public class TestUtils {

    public ActionContext createActionContext (final Class clazz) {
        ModelCacheService modelCacheService = new ModelCacheService();
        modelCacheService.close();
        modelCacheService.setConfigFile(new File("build/test/" + clazz.getSimpleName()));
        modelCacheService.set(new ModelCache(), "create action context");

        ActionContext actionContext = Mockito.mock(ActionContext.class);
        Mockito.when(actionContext.getModelCacheService()).thenReturn(modelCacheService);
        Mockito.when(actionContext.getModelCache()).thenReturn(modelCacheService.get());

        return actionContext;
    }

}
