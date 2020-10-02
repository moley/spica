package org.spica.javaclient.actions.configuration;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.recovery.ResilientFileOutputStream;
import java.io.File;
import java.util.Iterator;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.Configuration;
import org.spica.javaclient.actions.*;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.model.Model;

import java.net.URL;
import java.nio.charset.Charset;

public class ShowStatusAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(ShowStatusAction.class);

    @Override public String getDisplayname() {
        return "Show status";
    }

    @Override
    public String getDescription() {
        return "Show current state";
    }

    public String getLogfilePath () {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        for (ch.qos.logback.classic.Logger logger : context.getLoggerList()) {
            for (Iterator<Appender<ILoggingEvent>> index = logger.iteratorForAppenders(); index.hasNext();) {
                Appender<ILoggingEvent> appender = index.next();

                if (appender instanceof FileAppender) {
                    FileAppender<ILoggingEvent> fa = (FileAppender<ILoggingEvent>)appender;
                    ResilientFileOutputStream rfos = (ResilientFileOutputStream)fa.getOutputStream();
                    File file = rfos.getFile();
                    return file.getAbsolutePath();
                }
            }
        }

        return "<none>";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        Model model = actionContext.getModel();

        try {
            URL leguanVersion = getClass().getClassLoader().getResource("spica-cli.version");
            String version = "not found";
            if (leguanVersion != null) {
                version = IOUtils.toString(leguanVersion, Charset.defaultCharset());
            }

            outputDefault("Spica version          : " + version);

        } catch (Throwable e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
        outputDefault("Java                   : " + System.getProperty("java.version"));
        outputDefault("Current server         : " + Configuration.getDefaultApiClient().getBasePath());
        outputDefault("Current model          : " + actionContext.getServices().getModelCacheService().getConfigFile().getAbsolutePath());
        outputDefault("Logfile                : " + getLogfilePath());

        outputDefault("");
        outputDefault("Event infos real       : " + model.getEventInfosReal().size());
        outputDefault("Event infos planned    : " + model.getEventInfosPlanned().size());
        outputDefault("Projects               : " + model.getProjectInfos().size());
        outputDefault("Tasks                  : " + model.getTaskInfos().size());
        outputDefault("MessageContainers      : " + model.getMessagecontainerInfos().size());
        outputDefault("Links                  : " + model.getLinkInfos().size());
        outputDefault("Users                  : " + model.getUserInfos().size());




        return null;
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.CONFIGURATION;
    }

    @Override
    public Command getCommand() {
        return new Command("state", "s");
    }
}
