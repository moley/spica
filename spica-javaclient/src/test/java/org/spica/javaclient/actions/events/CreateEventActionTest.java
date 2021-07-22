package org.spica.javaclient.actions.events;

import java.time.LocalDate;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.spica.ModelBasedTest;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.EventType;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

public class CreateEventActionTest extends ModelBasedTest {

  @Test
  public void createBookingForToDay () {

    ActionContext actionContext = createNewActionContext();
    CommandLineArguments commandLineArguments = new CommandLineArguments();

    Assert.assertTrue(actionContext.getModel().getEventInfosReal().isEmpty());

    CreateEventAction createEventAction = new CreateEventAction();

    InputParams inputParams = createEventAction.getInputParams(actionContext, commandLineArguments);
    inputParams.getInputParam(CreateEventAction.KEY_TYPE).setValue(EventType.PAUSE.name());

    createEventAction.execute(actionContext, inputParams, commandLineArguments);

    Assert.assertFalse(actionContext.getModel().getEventInfosReal().isEmpty());

    EventInfo eventInfo = actionContext.getModel().getEventInfosReal().get(0);
    Assert.assertEquals(LocalDate.now().getDayOfMonth(), eventInfo.getStart().getDayOfMonth());
    Assert.assertEquals(LocalDate.now().getMonth(), eventInfo.getStart().getMonth());
    Assert.assertEquals (LocalDate.now().getYear(), eventInfo.getStart().getYear());

  }

  @Test
  public void createBookingForDifferentDay () {
    ActionContext actionContext = createNewActionContext();
    CommandLineArguments commandLineArguments = new CommandLineArguments();

    Assert.assertTrue(actionContext.getModel().getEventInfosReal().isEmpty());

    CreateEventAction createEventAction = new CreateEventAction();

    InputParams inputParams = createEventAction.getInputParams(actionContext, commandLineArguments);
    inputParams.getInputParam(CreateEventAction.KEY_DAY).setValue("1208");
    inputParams.getInputParam(CreateEventAction.KEY_TYPE).setValue(EventType.PAUSE.name());

    createEventAction.execute(actionContext, inputParams, commandLineArguments);

    Assert.assertFalse(actionContext.getModel().getEventInfosReal().isEmpty());

    EventInfo eventInfo = actionContext.getModel().getEventInfosReal().get(0);
    Assert.assertEquals(12, eventInfo.getStart().getDayOfMonth());
    Assert.assertEquals(8, eventInfo.getStart().getMonth().getValue());
    Assert.assertEquals (LocalDate.now().getYear(), eventInfo.getStart().getYear());

  }


}
