package org.spica.javaclient.actions.params;

import java.util.ArrayList;
import java.util.List;

public class InputParams {

    private List<InputParamGroup> inputParamGroups = new ArrayList<InputParamGroup>();

    public List<InputParamGroup> getInputParamGroups() {
        return inputParamGroups;
    }

    public InputParams () {

    }

    public InputParams (final List<InputParamGroup> inputParamGroups) {
        this.inputParamGroups = inputParamGroups;
    }

    public void setInputParamGroups(List<InputParamGroup> inputParamGroups) {
        this.inputParamGroups = inputParamGroups;
    }

    public Object getInputParam (final String key) {
        for (InputParamGroup next: inputParamGroups) {
            for (InputParam nextParam: next.getInputParams()) {
                if (nextParam.getKey().equals(key))
                    return nextParam.getValue();
            }
        }

        throw new IllegalStateException("Parameter with key " + key + " not found");
    }

    public String getInputParamAsString (final String key) {
        return getInputParam(key).toString();
    }

    public boolean isEmpty () {
        return inputParamGroups.isEmpty();
    }
}
