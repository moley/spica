package org.spica.javaclient.actions.params;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InputParams {

    private List<InputParamGroup> inputParamGroups = new ArrayList<InputParamGroup>();

    public List<InputParamGroup> getInputParamGroups() {
        return inputParamGroups;
    }

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public InputParams () {
        startTime = LocalDateTime.now();
    }

    public InputParams (final List<InputParamGroup> inputParamGroups) {
        this ();
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

    public boolean isAvailable (final String key) {
        return getInputParam(key) != null;
    }

    public String getInputParamAsString (final String key) {
        Object inputParam = getInputParam(key);
        return inputParam != null ? getInputParam(key).toString() : null;
    }

    public boolean isEmpty () {
        return inputParamGroups.isEmpty();
    }

    public String toString () {
        String asString = "";
        asString += "From : " + startTime + "\n";
        asString += "Until : " + endTime + "\n";
        for (InputParamGroup nextGroup: getInputParamGroups()) {
            asString += "Group: \n";
            for (InputParam nextParam: nextGroup.getInputParams()) {
                asString += "- " + nextParam.getKey() + " -> " + nextParam.getValue() + "\n";
            }
        }

        return asString;

    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
