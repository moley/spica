package org.spica.javaclient.actions;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.spica.javaclient.utils.LogUtil;

@Slf4j
public abstract class AbstractAction implements Action {

    private List<String> errors = new ArrayList<String>();

    protected void outputOk (final String message) {
        System.out.println (LogUtil.green(message));
    }

    protected void outputDefault (final String message) {
        System.out.println (message);
    }


    protected AbstractAction outputError (final String message) {
        errors.add(message);
        System.out.println (LogUtil.red(message));
        return this;
    }

    protected AbstractAction outputWarning (final String message) {
        errors.add(message);
        System.out.println (LogUtil.yellow(message));
        return this;
    }

    public void finish () {
        System.exit(1);
    }

    public List<String> getErrors () {
        return errors;
    }

    protected String getClipBoard(){
        try {
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (HeadlessException e) {
            outputError(e.getLocalizedMessage());
        } catch (UnsupportedFlavorException e) {
            outputError(e.getLocalizedMessage());
        } catch (IOException e) {
            outputError(e.getLocalizedMessage());
        }
        return null;
    }

}
