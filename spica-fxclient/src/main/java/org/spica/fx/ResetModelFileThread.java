package org.spica.fx;

import com.sun.nio.file.SensitivityWatchEventModifier;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import lombok.SneakyThrows;
import org.spica.fx.controllers.AbstractFxController;
import org.spica.javaclient.actions.ActionContext;

public class ResetModelFileThread extends Thread {

  private boolean stopped = false;

  private final File modelFile;
  private final ActionContext actionContext;
  private final Collection<AbstractFxController> controllers;

  public ResetModelFileThread(final File modelFile, final ActionContext actionContext, Collection<AbstractFxController> controllers) {
    this.modelFile = modelFile;
    this.actionContext = actionContext;
    this.controllers = controllers;
  }

  @SneakyThrows public void run(){
    System.out.println("ResetModelFileThread running");

    Path path = Paths.get(modelFile.getParentFile().toURI());
    WatchService watchService =  path.getFileSystem().newWatchService();
    path.register(watchService, new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_MODIFY}, SensitivityWatchEventModifier.HIGH);
    WatchKey watchKey = null;

    while (! stopped) {

      watchKey = watchService.poll(10, TimeUnit.MINUTES);
      if(watchKey != null) {
        watchKey.pollEvents().stream().forEach(event -> {
          System.out.println ("ResetModelFileThread recieved file event on " + event.context());
          Platform.runLater(new Runnable() {
            @Override public void run() {
              if (event.context().toString().equals("config.xml")) {
                System.out.println ("ResetModelFileThread recieved file event on " + event.context());
                actionContext.reloadModel();
                for (AbstractFxController nextController: controllers) {
                  nextController.setActionContext(actionContext);
                }
              }
            }
          });

        });
        watchKey.reset();
      }

      sleep(100);
    }
  }

  public void dispose () {
    stopped = true;
  }
}
