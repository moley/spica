package org.spica.javaclient.actions;

import java.io.File;
import org.spica.commons.SpicaProperties;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.UserInfo;
import org.spica.javaclient.params.ActionParamFactory;
import org.spica.javaclient.services.Services;

/**
 * Root model interface.
 * This is mapped to the property *spica* in an groovy script
 */
public interface ActionContext {

    /**
     * get the current working dir
     * @return current working dir
     */
    File getCurrentWorkingDir ();

    /**
     * get the model with access to e.g. projects, tasks,...
     * @return model
     */
    Model getModel();

    /**
     * get services which are available in spica, e.g. jenkinservice, bitbucket service
     * This means, which don't need the spica server to be used
     *
     * @return services object
     */
    Services getServices ();

    /**
     * get apis of the spica server.
     * This means services, which are called on the spica server itself, REST API of spica server
     * @return apis object
     */
    Api getApi ();

    /**
     * get spica properties to be used, e.g. credentials.
     * For each spica property a constant class is provided
     *
     * @return properties
     */
    SpicaProperties getProperties();

    /**
     * get access to the action handler. With this you get access to
     * actions which are provided to the command line interface
     * <b>Should not be used from groovy scripts</b>
     * @return action handler
     */
    ActionHandler getActionHandler();

    /**
     * get access to the action parameter factory, which handles asking for parameters in
     * commandline actions. <b>Should not be used from groovy scripts</b>
     * @return action parameter factory
     */
    ActionParamFactory getActionParamFactory();

    /**
     * save the model
     * @param lastAction parameter is used to describe what the last change of model was
     */
    void saveModel(final String lastAction);




}
