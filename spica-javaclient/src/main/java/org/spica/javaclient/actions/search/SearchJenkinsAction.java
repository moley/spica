package org.spica.javaclient.actions.search;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.FlagInputParam;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;

public class SearchJenkinsAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(SearchJenkinsAction.class);

    private final static String KEY_TESTS = "tests";

    @Override public String getDisplayname() {
        return "Goto jenkins";
    }

    @Override
    public String getDescription() {
        return "Navigates to jenkins of your current working dir if Jenkinsfile with link available";
    }


    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        String detailsSite = "";
        if (inputParams.getInputValueAsBoolean(KEY_TESTS, false)) {
            detailsSite = "/lastCompletedBuild/testReport/";
        }


        File currentWorkingDir = actionContext.getCurrentWorkingDir();
        File jenkinsFile = new File (currentWorkingDir, "Jenkinsfile");
        if (jenkinsFile.exists()) {
            try {
                List<String> lines = FileUtils.readLines(jenkinsFile, Charset.defaultCharset());
                String link = lines.get(0).startsWith("#") ? lines.get(1): lines.get(0); //in the first line after optional shebang
                if (link.startsWith("//")) {
                    link = link.substring(2).replace("//", "/");
                    String completeLink = (link + detailsSite).trim();
                    Desktop.getDesktop().browse(new URI(completeLink));
                    return null;
                }
                else
                    outputError("In file " + jenkinsFile.getAbsolutePath() + " there is no or no commented link in this file available. Please create one and put the url to the buildjob as comment in the line after shebang.");

            } catch (IOException | URISyntaxException e) {
                throw new IllegalStateException(e);
            }

        }
        else
            outputError("In path " + currentWorkingDir.getAbsolutePath() + " there is no Jenkinsfile available");

        return null;
    }

    @Override
    public ActionGroup getGroup() {
        return ActionGroup.SEARCH;
    }

    @Override
    public Command getCommand() {
        return new Command("jenkins", "j");
    }

    @Override public InputParams getInputParams(ActionContext actionContext, CommandLineArguments commandLineArguments) {
        InputParams inputParams = new InputParams();
        InputParamGroup inputParamGroup = new InputParamGroup();

        inputParamGroup.getInputParams().add(new FlagInputParam(KEY_TESTS));

        inputParams.getInputParamGroups().add(inputParamGroup);

        return inputParams;
    }
}
