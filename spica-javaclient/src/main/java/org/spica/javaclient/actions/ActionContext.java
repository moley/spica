package org.spica.javaclient.actions;

import org.spica.commons.SpicaProperties;
import org.spica.javaclient.model.ModelCache;

import java.io.File;

public interface ActionContext {

    File getCurrentWorkingDir ();

    ModelCache getModelCache ();

    SpicaProperties getSpicaProperties ();
}
