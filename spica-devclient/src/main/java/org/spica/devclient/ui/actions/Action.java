package org.spica.devclient.ui.actions;

import javafx.scene.control.Button;

public interface Action {

    Button getButton ();

    String getDisplayname ();

    void execute ();
}
