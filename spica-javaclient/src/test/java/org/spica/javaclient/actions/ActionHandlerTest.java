package org.spica.javaclient.actions;

import org.junit.Assert;
import org.junit.Test;
import org.spica.javaclient.actions.booking.CreateBookingAction;
import org.spica.javaclient.actions.tasks.ListTasksAction;

import java.util.Collection;

public class ActionHandlerTest {

    @Test
    public void findActionInvalidCommand () {
        ActionHandler actionHandler = new ActionHandler();
        Assert.assertNull (actionHandler.findAction("x s"));

    }

    @Test(expected = IllegalStateException.class)
    public void findActionOneToken () {
        ActionHandler actionHandler = new ActionHandler();
        actionHandler.findAction("t");

    }
    @Test
    public void findAction () {
        ActionHandler actionHandler = new ActionHandler();
        FoundAction foundAction = actionHandler.findAction("t l");
        Assert.assertEquals ("Wrong action found: ", ListTasksAction.class, foundAction.getAction().getClass());
    }

    @Test
    public void getHelp () {
        ActionHandler actionHandler = new ActionHandler();
        CreateBookingAction createBookingAction = new CreateBookingAction();
        Collection<String> helpStrings = actionHandler.getHelp(ActionGroup.BOOKING);
        String asString = String.join("\n", helpStrings);
        Assert.assertTrue ("Shortform of action not found", asString.contains("b c"));
        Assert.assertTrue ("Description of action not found", asString.contains(createBookingAction.getDescription()));

    }
}
