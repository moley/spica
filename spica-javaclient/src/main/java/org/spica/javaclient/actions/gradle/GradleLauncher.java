package org.spica.javaclient.actions.gradle;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.spica.javaclient.utils.LogUtil;

public class GradleLauncher {

  private File workingDir;

  private List<String> tasks;

  private String id;

  private List<String> output = new ArrayList<>();

  private List<String> errorOutput = new ArrayList<>();

  private int returnCode = -99;

  private String thread = Thread.currentThread().getName();

  private long duration = -1;

  public File getWorkingDir() {
    return workingDir;
  }

  public void setWorkingDir(File workingDir) {
    this.workingDir = workingDir;
  }

  public List<String> getTasks() {
    return tasks;
  }

  public void setTasks(List<String> tasks) {
    this.tasks = tasks;
  }

  public void execute () {

    List<String> parameters = new ArrayList<>(tasks);
    parameters.add(0, "gradlew");
    parameters.add("--stacktrace");

    long start = System.currentTimeMillis();

    ProcessBuilder processBuilder = new ProcessBuilder().command(parameters);
    processBuilder.directory(workingDir);
    try {
      Process process = processBuilder.start();
      try (BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
        String line = null;
        while ((line = inputStreamReader.readLine()) != null) {
          output(line);
        }
      }

      try (BufferedReader errorStreamReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
        String line = null;
        while ((line = errorStreamReader.readLine()) != null) {
          error(line);

        }
      }
      returnCode = process.waitFor();
      if (returnCode != 0) {
        error("Process " + processBuilder.command() + " in path " + workingDir.getAbsolutePath() + " finished with returncode " + returnCode);
      }

    } catch (IOException | InterruptedException e) {
      throw new IllegalStateException("Error in command " + processBuilder.command() + " in path " + workingDir.getAbsolutePath() + ": " + e.getLocalizedMessage(), e);
    } finally {
      long end = System.currentTimeMillis();
      duration = end - start;
    }

  }

  public void output (final String message) {
    output.add(message);
    System.out.println("["  + thread + "#" + id + "]   " + message);
  }

  public void error (final String message) {
    errorOutput.add(message);
    System.out.println(LogUtil.red("[" + thread + "#" + id + "]   " + message));
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public long getDuration() {
    return duration;
  }

  public int getReturnCode () {
    return returnCode;
  }
}
