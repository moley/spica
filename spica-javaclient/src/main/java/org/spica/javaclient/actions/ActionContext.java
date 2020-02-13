package org.spica.javaclient.actions;

import java.io.File;
import org.spica.commons.SpicaProperties;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.UserInfo;
import org.spica.javaclient.params.ActionParamFactory;
import org.spica.javaclient.services.Services;

public interface ActionContext {

    File getCurrentWorkingDir ();

    Model getModel();

    Model reloadModel ();

    Services getServices ();

    Api getApi ();

    SpicaProperties getProperties();

    ActionHandler getActionHandler();

    ActionParamFactory getActionParamFactory();

    void saveModel(final String lastAction);




}
