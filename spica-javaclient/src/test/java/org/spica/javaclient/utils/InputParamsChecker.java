package org.spica.javaclient.utils;

import org.junit.Assert;
import org.spica.javaclient.actions.params.InputParams;

public class InputParamsChecker {

    private InputParams inputParams;

    public InputParamsChecker (final InputParams inputParams) {
        this.inputParams = inputParams;
    }

    public InputParamsChecker numberOfGroups (final int checkNumber) {
        Assert.assertEquals (this.inputParams.getInputParamGroups().size(), checkNumber);
        return this;
    }

    public InputParamsChecker inputParam (final String paramKey) {
        this.inputParams.isInputParamAvailable(paramKey);
        return this;
    }
}
