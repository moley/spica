package org.spica.javaclient.actions.gradle;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.FlagInputParam;
import org.spica.javaclient.params.InputParamGroup;
import org.spica.javaclient.params.InputParams;
import org.spica.javaclient.params.TextInputParam;
import org.spica.javaclient.services.DownloadService;

public class InitGradleProjectAction extends AbstractAction {

  private final static Logger LOGGER = LoggerFactory.getLogger(InitGradleProjectAction.class);

  private final static String KEY_VERSION = "version";
  private final static String KEY_ALL = "all";

  @Override public String getDisplayname() {
    return "Init gradle project";
  }

  @Override public String getDescription() {
    return "Create gradle wrapper in a project";
  }

  @Override public ActionResult execute(ActionContext actionContext, InputParams inputParams,
      CommandLineArguments commandLineArguments) {

    String version = inputParams.getInputValueAsString(KEY_VERSION);
    if (! version.contains("/"))
      version = "https://services.gradle.org/distributions/gradle-" + version + "-bin.zip";
    boolean all = inputParams.getInputValueAsBoolean(KEY_ALL, false);

    File wrapperPath = new File (actionContext.getCurrentWorkingDir(), "gradle/wrapper");

    DownloadService downloadService = actionContext.getServices().getDownloadService();

    final String baseRemote = "https://github.com/moley/spica/raw/master/";
    try {
      downloadService.download(actionContext.getCurrentWorkingDir(), "gradlew", baseRemote + "gradlew", true);
      downloadService.download(actionContext.getCurrentWorkingDir(), "gradlew.bat", baseRemote + "gradlew.bat", true);

      downloadService.download(wrapperPath, "gradle-wrapper.jar", baseRemote + "gradle/wrapper/gradle-wrapper.jar", false);
      downloadService.download(wrapperPath, "gradle-wrapper.properties", baseRemote + "gradle/wrapper/gradle-wrapper.properties", false);

      File wrapperProperties = new File (wrapperPath, "gradle-wrapper.properties");
      Properties properties = new Properties();
      properties.load(new FileReader(wrapperProperties));
      properties.setProperty("distributionUrl", version);
      properties.store(new FileWriter(wrapperProperties), "Created by spica");


    } catch (IOException e) {
      throw new IllegalStateException("Error downloading gradle wrapper files", e);
    }

    outputOk("Init gradle project in folder " + actionContext.getCurrentWorkingDir().getAbsolutePath());
    outputOk("using " + version + "(all = " + all + ")");

    return null;
  }

  @Override public ActionGroup getGroup() {
    return ActionGroup.GRADLE;
  }

  @Override public Command getCommand() {
    return new Command("init", "i");
  }

  @Override public InputParams getInputParams(ActionContext actionContext, CommandLineArguments commandLineArguments) {
    InputParams inputParams = new InputParams();
    InputParamGroup inputParamGroup = new InputParamGroup();

    String firstArgument = commandLineArguments.getOptionalMainArgument();
    if (firstArgument != null && firstArgument.startsWith("-"))
      firstArgument = null;

    inputParamGroup.getInputParams().add(new TextInputParam(5, KEY_VERSION, "Version (url or versionstring)", firstArgument));
    inputParamGroup.getInputParams().add(new FlagInputParam(KEY_ALL));

    inputParams.getInputParamGroups().add(inputParamGroup);

    return inputParams;
  }

}
