package org.spica.javaclient.actions.imports;

import com.google.common.io.Files;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;
import org.spica.javaclient.FileUtil;
import org.spica.javaclient.actions.AbstractAction;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.ActionResult;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.params.CommandLineArguments;
import org.spica.javaclient.params.InputParams;

public class ImportLogfilesAction extends AbstractAction {

  private final static Logger LOGGER = LoggerFactory.getLogger(ImportLogfilesAction.class);

  private FileUtil fileUtil = new FileUtil();

  @Override public String getDisplayname() {
    return "Import logfiles";
  }

  @Override public String getDescription() {
    return "Imports logfile in current path (param is name, param 'stack' filters all logfiles with stacktrace, param 'all' imports all)";
  }

  public boolean containsStacktrace (final File file) {
    try {
      Collection<String> content = FileUtils.readLines(file, Charset.defaultCharset());
      for (String next: content) {
        if (next.trim().startsWith("at ") && next.trim().endsWith(")"))
          return true;
      }
    } catch (IOException e) {
      throw new IllegalStateException("Error reading logfile " + file.getAbsolutePath(), e);
    }
    return false;
  }



  @Override public ActionResult execute(ActionContext actionContext, InputParams inputParams, CommandLineArguments commandLineArguments) {

    String param = commandLineArguments.getOptionalMainArgument();
    boolean all = param != null && param.equals("all");
    boolean stack = param != null && param.equals("stack");
    outputOk("All " + all + ",Stack " + stack + ", Filter " + param);

    Collection<File> importedFiles = new ArrayList<File>();

    File rootPath = new File ("").getAbsoluteFile();
    for (File next: FileUtils.listFiles(rootPath, new String [] {"log"}, true)) {
      String localFile = fileUtil.getRelativeName(rootPath, next);

      boolean containsStack = containsStacktrace(next);
      if (containsStack)
        outputWarning("Found file " + localFile + "(contains stack)");
      else
        outputOk("Found file " + localFile);

      if (all || (param != null && localFile.equals(param))) {
        importedFiles.add(next);
      }
      else if (stack && containsStacktrace(next)) {
        importedFiles.add(next);
      }
    }

    outputOk("");

    if (importedFiles.isEmpty())
      outputError("Please add param with name of logfile to import one file, param 'all' to import all files or param 'stack' to import only files with stacktraces");
    else {
      for (File next: importedFiles) {
        outputOk("Import " + next.getAbsolutePath());
      }
    }

    File importedFile = new File (SpicaProperties.getImportFolder(), UUID.randomUUID().toString() + ".zip");
    try {
      ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(importedFile));
      for (File next: importedFiles) {
        String localFile = fileUtil.getRelativeName(rootPath, next);
        ZipEntry zipEntry = new ZipEntry(localFile);
        zos.putNextEntry(zipEntry);
        zos.write(FileUtils.readFileToByteArray(next));
        zos.closeEntry();
      }

      zos.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

    outputOk("Imported " + importedFiles.size() + " files as " + importedFile.getAbsolutePath());

    actionContext.saveModel(getClass().getName());
    return null;
  }

  @Override public ActionGroup getGroup() {
    return ActionGroup.IMPORT;
  }

  @Override public Command getCommand() {
    return new Command("logs", "l");
  }
}