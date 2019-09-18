package org.spica.javaclient.actions.params;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class InputParamGroup {

    private final Predicate<InputParams> activationPredicate;

    private final String name;

    public InputParamGroup (String name, Predicate<InputParams> activationPredicate) {
        this.activationPredicate = activationPredicate;
        this.name = name;
    }

    public InputParamGroup () {
        this.activationPredicate = null;
        this.name = null;
    }

    public InputParamGroup (String name) {
        this.activationPredicate = null;
        this.name = name;
    }

    List<InputParam> inputParams = new ArrayList<InputParam>();

    public List<InputParam> getInputParams () {
        return inputParams;
    }

    public Predicate<InputParams> getActivationPredicate() {
        return activationPredicate;
    }

    public String getName() {
        return name;
    }
}
