package org.spica.javaclient.utils;

import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.mockito.Mockito;
import org.spica.commons.SpicaProperties;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.services.ModelCacheService;

import java.io.File;

public class TestUtils {

    public ActionContext createActionContext (final Class clazz) {
        ModelCacheService modelCacheService = new ModelCacheService();
        modelCacheService.close();
        modelCacheService.setConfigFile(new File("build/test/" + clazz.getSimpleName()));
        modelCacheService.save(new Model(), "create action context");

        ActionContext actionContext = Mockito.mock(ActionContext.class);
        Mockito.when(actionContext.getServices().getModelCacheService()).thenReturn(modelCacheService);
        Mockito.when(actionContext.getModel()).thenReturn(modelCacheService.load());

        return actionContext;
    }

    public void setupJunitTestWorkspace (final String classname) throws IOException {
        File workspace = new File("build/junit/" + getClass().getSimpleName());
        if (workspace.exists())
          FileUtils.deleteDirectory(workspace);
        SpicaProperties.setSpicaHome(workspace);
    }

}
