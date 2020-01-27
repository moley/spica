package org.spica.javaclient.actions.auto;

import groovy.util.ResourceException;
import groovy.util.ScriptException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.params.TextInputParam;
import org.spica.javaclient.scripting.ScriptEngine;

public class ExecuteScriptAction extends AbstractAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExecuteScriptAction.class);

    private static String KEY_FILE = "file";


    @Override public String getDisplayname() {
        return "Execute script";
    }

    @Override
    public String getDescription() {
        return "Execute script";
    }

    @Override
    public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

        final String branch = inputParams.getInputValueAsString(KEY_FILE);
        File file = new File (branch);
        if (! file.exists())
            throw new IllegalStateException("File " + file.getAbsolutePath() + " does not exist");

        ScriptEngine scriptEngine = new ScriptEngine();
        scriptEngine.setActionContext(actionContext);
        scriptEngine.setGroovyFile(file);
        try {
            scriptEngine.execute();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (ResourceException e) {
            throw new IllegalStateException(e);
        } catch (ScriptException e) {
            throw new IllegalStateException(e);
        }

        return null;
    }


    @Override
    public ActionGroup getGroup() {
        return ActionGroup.AUTOMATION;
    }

    @Override
    public Command getCommand() {
        return new Command ("execute", "e");
    }

    @Override public InputParams getInputParams(ActionContext actionContext, CommandLineArguments commandLineArguments) {



        InputParamGroup inputParamGroup = new InputParamGroup();
        InputParams inputParams = new InputParams(Arrays.asList(inputParamGroup));

        TextInputParam branch = new TextInputParam(1, KEY_FILE, "File", commandLineArguments.getOptionalMainArgument());
        inputParamGroup.getInputParams().add(branch);

        return inputParams;
    }


}
