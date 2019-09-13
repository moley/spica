package org.spica.cli.actions;

import de.codeshelf.consoleui.elements.PromptableElementIF;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.InputResult;
import de.codeshelf.consoleui.prompt.PromtResultItemIF;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import jline.console.completer.Completer;
import jline.console.completer.StringsCompleter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.FoundAction;
import org.spica.javaclient.actions.params.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandaloneActionParamFactory implements ActionParamFactory {

    private final static Logger LOGGER = LoggerFactory.getLogger(StandaloneActionParamFactory.class);


    @Override
    public InputParams build(ActionContext actionContext, FoundAction foundAction) {

        InputParams inputParams = foundAction.getAction().getInputParams(actionContext);

        ConsolePrompt prompt = new ConsolePrompt();

        PromptBuilder promptBuilder = prompt.getPromptBuilder();

        List<InputParamGroup> inputParamGroups = inputParams.getInputParamGroups();
        for (InputParamGroup nextGroup: inputParamGroups) {
            for (InputParam nextInputParam: nextGroup.getInputParams()) {

                if (nextInputParam instanceof SearchInputParam) {
                    SearchInputParam<String> searchInputParam = (SearchInputParam<String>) nextInputParam;
                    StringsCompleter stringsCompleter = new StringsCompleter(searchInputParam.getItems());
                    promptBuilder.createInputPrompt().name(nextInputParam.getKey()).message(nextInputParam.getDisplayname()).addCompleter(stringsCompleter).addPrompt();
                }
                else if (nextInputParam instanceof TextInputParam) {
                    promptBuilder.createInputPrompt().name(nextInputParam.getKey()).message(nextInputParam.getDisplayname()).addPrompt();
                }
                else
                    throw new IllegalStateException("Param type " + nextInputParam.getClass().getName() + " not supported");
            }
        }



        List<PromptableElementIF> elements = promptBuilder.build();

        try {
            HashMap<String, ? extends PromtResultItemIF> nameResult = prompt.prompt(elements);

            for (InputParamGroup nextGroup: inputParamGroups) {
                for (InputParam nextInputParam : nextGroup.getInputParams()) {
                    PromtResultItemIF promtResultItemIF = nameResult.get(nextInputParam.getKey());
                    if (promtResultItemIF instanceof InputResult) {
                        InputResult inputResult = (InputResult) promtResultItemIF;
                        nextInputParam.setValue(inputResult.getInput());
                    }
                    else
                        throw new IllegalStateException("Result of type " + promtResultItemIF.getClass().getName() + " not supported");


                }
            }
        } catch (IOException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }

        return inputParams;

    }
}
