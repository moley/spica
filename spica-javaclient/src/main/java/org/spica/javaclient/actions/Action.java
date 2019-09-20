package org.spica.javaclient.actions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.params.InputParams;
import org.spica.javaclient.utils.LogUtil;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;


public interface Action {

    final static Logger LOGGER = LoggerFactory.getLogger(Action.class);

    String getDisplayname ();

    String getDescription ();

    void execute (ActionContext actionContext, InputParams inputParams, String parameterList);

    default String getFirstValue (final String ... values) {
        for (String next: values) {
            System.out.println ("Check value " + next);
            if (next != null && ! next.trim().isBlank())
                return next;
        }
        throw new IllegalArgumentException("No value of " + values + " was valid");
    }

    ActionGroup getGroup ();

    Command getCommand ();

    default InputParams getInputParams(ActionContext actionContext, String parameterList) {
        LOGGER.info("default getInputParams implementation");
        return new InputParams();
    }

    default void beforeParam (ActionContext actionContext, String parameterList) {
        LOGGER.info("default beforeParam implementation");
    }

    default void outputOk (final String message) {
        System.out.println (LogUtil.green(message));
    }

    default void outputDefault (final String message) {
        System.out.println (message);
    }

    default void outputError (final String message) {
        System.out.println (LogUtil.red(message));
    }

    default String getClipBoard(){
        try {
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (HeadlessException e) {
            outputError(e.getLocalizedMessage());
        } catch (UnsupportedFlavorException e) {
            outputError(e.getLocalizedMessage());
        } catch (IOException e) {
            outputError(e.getLocalizedMessage());
        }
        return "";
    }
}
