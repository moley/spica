package org.spica.javaclient.scripting;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import java.io.File;
import java.io.IOException;
import org.spica.javaclient.actions.ActionContext;

public class ScriptEngine {

  private ActionContext actionContext;


  private File groovyFile;

  public void execute() throws IOException, ResourceException, ScriptException {

    Binding binding = new Binding();
    binding.setProperty("spica", actionContext);
    GroovyScriptEngine groovyScriptEngine = new GroovyScriptEngine(groovyFile.getAbsolutePath(), ScriptEngine.class.getClassLoader());

    groovyScriptEngine.run(groovyFile.getName(), binding);


  }

  public File getGroovyFile() {
    return groovyFile;
  }

  public void setGroovyFile(File groovyFile) {
    this.groovyFile = groovyFile;
  }


  public ActionContext getActionContext() {
    return actionContext;
  }

  public void setActionContext(ActionContext modelCache) {
    this.actionContext = modelCache;
  }
}
