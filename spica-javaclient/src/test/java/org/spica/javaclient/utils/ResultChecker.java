package org.spica.javaclient.utils;

import org.junit.Assert;
import org.spica.javaclient.actions.AbstractAction;

public class ResultChecker {

    private AbstractAction abstractAction;

    public ResultChecker (final AbstractAction abstractAction) {
        this.abstractAction = abstractAction;
    }

    public ResultChecker numberOfErrors (final int expectedNumberOfErrors) {
        Assert.assertEquals ("Number of errors invalid", expectedNumberOfErrors, this.abstractAction.getErrors().size());
        return this;
    }

    public ResultChecker contains (final String errorMessage) {
        for (String next: this.abstractAction.getErrors()) {
            if (next.contains(errorMessage))
                return this;
        }

        Assert.fail ("Error message <" + errorMessage + "> not found among errors " + this.abstractAction.getErrors());
        return this;
    }
}
