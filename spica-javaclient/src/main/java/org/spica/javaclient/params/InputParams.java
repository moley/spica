package org.spica.javaclient.params;

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

    public boolean isInputParamAvailable(final String key) {
        for (InputParamGroup next: inputParamGroups) {
            for (InputParam nextParam: next.getInputParams()) {
                if (nextParam.getKey().equals(key))
                    return true;
            }
        }

        return false;
    }

    public Object getInputValue(final String key) {
        for (InputParamGroup next: inputParamGroups) {
            for (InputParam nextParam: next.getInputParams()) {
                if (nextParam.getKey().equals(key))
                    return nextParam.getValue();
            }
        }

        return null;
    }

    public <T> T getInputValue(final String key, final Class<T> type) {
        return type.cast(getInputValue(key));
    }

    public boolean isAvailable (final String key) {
        return getInputValue(key) != null;
    }

    public String getInputValueAsString(final String key) {
        Object inputParam = getInputValue(key);
        return inputParam != null ? getInputValue(key).toString() : null;
    }

    public boolean getInputValueAsBoolean(final String key, final boolean defaultValue) {
        String valueAsString = getInputValueAsString(key);
        if (valueAsString == null)
            return defaultValue;
        else
        return valueAsString.equalsIgnoreCase(Boolean.TRUE.toString()) || valueAsString.equalsIgnoreCase("YES");
    }

    public boolean isEmpty () {
        return inputParamGroups.isEmpty();
    }

    public String toString () {
        String asString = "";
        asString += "From : " + startTime + "\n";
        asString += "Until : " + endTime + "\n";
        for (InputParamGroup nextGroup: getInputParamGroups()) {
            asString += "Group (" + nextGroup.getName() + "): \n";
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
