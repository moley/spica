package org.spica.cli.actions;

import de.codeshelf.consoleui.elements.PromptableElementIF;
import de.codeshelf.consoleui.prompt.CheckboxResult;
import de.codeshelf.consoleui.prompt.ConfirmResult;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.InputResult;
import de.codeshelf.consoleui.prompt.ListResult;
import de.codeshelf.consoleui.prompt.PromtResultItemIF;
import de.codeshelf.consoleui.prompt.builder.CheckboxPromptBuilder;
import de.codeshelf.consoleui.prompt.builder.ListPromptBuilder;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import jline.console.completer.StringsCompleter;
import lombok.extern.slf4j.Slf4j;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.params.ActionParamFactory;
import org.spica.javaclient.params.ConfirmInputParam;
import org.spica.javaclient.params.FlagInputParam;
import org.spica.javaclient.params.InputParam;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.params.MultiSelectInputParam;
import org.spica.javaclient.params.SearchInputParam;
import org.spica.javaclient.params.SelectInputParam;
import org.spica.javaclient.params.TextInputParam;

@Slf4j
public class StandaloneActionParamFactory implements ActionParamFactory {


    @Override
    public InputParams build(ActionContext actionContext, InputParams inputParams) {

        ConsolePrompt prompt = new ConsolePrompt();

        List<InputParamGroup> inputParamGroups = inputParams.getInputParamGroups();
        for (InputParamGroup nextGroup : inputParamGroups) {

            System.out.println ("");

            PromptBuilder promptBuilder = prompt.getPromptBuilder();

            if (nextGroup.getActivationPredicate() != null && nextGroup.getActivationPredicate().test(inputParams) == false) {
                continue;
            }

            for (InputParam nextInputParam : nextGroup.getInputParams()) {

                if (nextInputParam.getValue() != null || ! nextInputParam.isVisible())
                    continue;

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
                        prompBuilder.newItem(nextKey).text(nextKey).add();
                    }
                    prompBuilder.addPrompt();
                } else if (nextInputParam instanceof MultiSelectInputParam) {
                    MultiSelectInputParam<Object> searchInputParam = (MultiSelectInputParam<Object>) nextInputParam;

                    CheckboxPromptBuilder prompBuilder = promptBuilder.createCheckboxPrompt().name(nextInputParam.getKey()).message(nextInputParam.getDisplayname());

                    for (String nextKey : searchInputParam.getItemsStringMap().keySet()) {
                        prompBuilder.newItem(nextKey).text(nextKey).add();
                    }
                    prompBuilder.addPrompt();

                } else if (nextInputParam instanceof ConfirmInputParam) {
                    promptBuilder.createConfirmPromp().name(nextInputParam.getKey()).message(nextInputParam.getDisplayname()).addPrompt();
                } else if (nextInputParam instanceof FlagInputParam) {
                    //do nothing because we have no value
                } else
                    throw new IllegalStateException("Param type " + nextInputParam.getClass().getName() + " not supported");
            }

            List<PromptableElementIF> elements = promptBuilder.build();

            try {
                HashMap<String, ? extends PromtResultItemIF> nameResult = prompt.prompt(elements);


                for (InputParam nextInputParam : nextGroup.getInputParams()) {
                    PromtResultItemIF promtResultItemIF = nameResult.get(nextInputParam.getKey());
                    if (promtResultItemIF != null) {
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


                }
            } catch (IOException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }


        return inputParams;

    }
}
