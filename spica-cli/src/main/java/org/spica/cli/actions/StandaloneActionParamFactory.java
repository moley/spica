package org.spica.cli.actions;

import de.codeshelf.consoleui.elements.PromptableElementIF;
import de.codeshelf.consoleui.prompt.*;
import de.codeshelf.consoleui.prompt.builder.ListPromptBuilder;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import jline.console.completer.StringsCompleter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.FoundAction;
import org.spica.javaclient.actions.params.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class StandaloneActionParamFactory implements ActionParamFactory {

    private final static Logger LOGGER = LoggerFactory.getLogger(StandaloneActionParamFactory.class);


    @Override
    public InputParams build(ActionContext actionContext, InputParams inputParams, FoundAction foundAction) {

        ConsolePrompt prompt = new ConsolePrompt();



        List<InputParamGroup> inputParamGroups = inputParams.getInputParamGroups();
        for (InputParamGroup nextGroup : inputParamGroups) {

            System.out.println ("");

            PromptBuilder promptBuilder = prompt.getPromptBuilder();

            if (nextGroup.getActivationPredicate() != null && nextGroup.getActivationPredicate().test(inputParams) == false) {
                continue;
            }

            for (InputParam nextInputParam : nextGroup.getInputParams()) {

                if (nextInputParam instanceof SearchInputParam) {
                    SearchInputParam<String> searchInputParam = (SearchInputParam<String>) nextInputParam;
                    StringsCompleter stringsCompleter = new StringsCompleter(searchInputParam.getItems());
                    promptBuilder.createInputPrompt().name(nextInputParam.getKey()).message(nextInputParam.getDisplayname()).addCompleter(stringsCompleter).addPrompt();
                } else if (nextInputParam instanceof TextInputParam) {
                    promptBuilder.createInputPrompt().name(nextInputParam.getKey()).message(nextInputParam.getDisplayname()).addPrompt();
                } else if (nextInputParam instanceof SelectInputParam) {
                    SelectInputParam<Object> searchInputParam = (SelectInputParam<Object>) nextInputParam;

                    ListPromptBuilder prompBuilder = promptBuilder.createListPrompt().name(nextInputParam.getKey()).message(nextInputParam.getDisplayname());

                    for (String nextKey : searchInputParam.getItemsStringMap().keySet()) {
                        String nextMessage = searchInputParam.getValueOf(nextKey);
                        prompBuilder.newItem(nextKey).text(nextMessage).add();
                    }
                    prompBuilder.addPrompt();
                } else if (nextInputParam instanceof ConfirmInputParam) {
                    promptBuilder.createConfirmPromp().name(nextInputParam.getKey()).message(nextInputParam.getDisplayname()).addPrompt();
                } else
                    throw new IllegalStateException("Param type " + nextInputParam.getClass().getName() + " not supported");
            }

            List<PromptableElementIF> elements = promptBuilder.build();

            try {
                HashMap<String, ? extends PromtResultItemIF> nameResult = prompt.prompt(elements);


                for (InputParam nextInputParam : nextGroup.getInputParams()) {
                    PromtResultItemIF promtResultItemIF = nameResult.get(nextInputParam.getKey());
                    if (promtResultItemIF instanceof InputResult) {
                        InputResult inputResult = (InputResult) promtResultItemIF;
                        nextInputParam.setValue(inputResult.getInput());
                    } else if (promtResultItemIF instanceof ListResult) {
                        ListResult listResult = (ListResult) promtResultItemIF;
                        nextInputParam.setValue(listResult.getSelectedId());
                    } else if (promtResultItemIF instanceof CheckboxResult) {
                        CheckboxResult inputResult = (CheckboxResult) promtResultItemIF;
                        nextInputParam.setValue(inputResult.getSelectedIds());
                    } else if (promtResultItemIF instanceof ConfirmResult) {
                        ConfirmResult confirmResult = (ConfirmResult) promtResultItemIF;
                        nextInputParam.setValue(confirmResult.getConfirmed().name());
                    } else
                        throw new IllegalStateException("Result of type " + promtResultItemIF.getClass().getName() + " not supported");


                }
            } catch (IOException e) {
                LOGGER.error(e.getLocalizedMessage(), e);
            }
        }


        return inputParams;

    }
}
