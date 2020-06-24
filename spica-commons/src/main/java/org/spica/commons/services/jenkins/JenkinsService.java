package org.spica.commons.services.jenkins;

import com.google.common.base.Optional;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.FolderJob;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.QueueReference;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;

public class JenkinsService {

  private static final Logger LOGGER = LoggerFactory.getLogger(JenkinsService.class);


  public final static String PROPERTY_JENKINS_URL = "spica.jenkins.url";
  public final static String PROPERTY_JENKINS_USER = "spica.jenkins.user";
  public final static String PROPERTY_JENKINS_PASSWORD = "spica.jenkins.password";


  public Jenkins connectToJenkinsServer() throws JenkinsException {

    SpicaProperties spicaProperties = new SpicaProperties();

    String jenkinsUrlString = spicaProperties.getValueNotNull(PROPERTY_JENKINS_URL);
    String jenkinsUser = spicaProperties.getValueNotNull(PROPERTY_JENKINS_USER);
    String jenkinsPassword = spicaProperties.getValueNotNull(PROPERTY_JENKINS_PASSWORD);

    URI jenkinsUri = URI.create(jenkinsUrlString);
    JenkinsServer jenkinsServer = new JenkinsServer(jenkinsUri, jenkinsUser, jenkinsPassword);
    if (!jenkinsServer.isRunning())
      throw new JenkinsException("Jenkins server " + jenkinsUrlString + " with user access (user " + jenkinsUser + ") is not running");

    Jenkins jenkins = new Jenkins(jenkinsServer, jenkinsUser);

    return jenkins;
  }

  public String triggerBuild (Jenkins jenkins, String foldername, String jobname) {
    return triggerBuild(jenkins, foldername, jobname, null);
  }

  public String triggerBuild (Jenkins jenkins, String foldername, String jobname, final HashMap<String, String> parameters) {
    LOGGER.info("Trigger build in folder " + foldername + ":" + jobname);
    JenkinsServer jenkinsServer = jenkins.getJenkinsServer();

    FolderJob folderJob = getFolderJob(jenkins, foldername);
    JobWithDetails job = null;
    try {
      job = jenkinsServer.getJob(folderJob, jobname);
      QueueReference queueReference = null;

      if (parameters == null)
        queueReference = job.build(true);
      else
        queueReference = job.build(parameters, true);

      return queueReference.getQueueItemUrlPart();
    } catch (IOException e) {
      throw new JenkinsException("Error triggering build " + foldername + "/" + jobname, e);
    }
  }

  private FolderJob getFolderJob(final Jenkins jenkins, final String folder) {

    if (folder == null)
      return null;

    JenkinsServer jenkinsServer = jenkins.getJenkinsServer();
    try {
      JobWithDetails folderJob = jenkinsServer.getJob(folder);
      if (folderJob == null) {
        jenkinsServer.createFolder(folder);
        folderJob = jenkinsServer.getJob(folder);
      }

      Optional<FolderJob> folderJobIfExists = jenkinsServer.getFolderJob(folderJob);
      if (!folderJobIfExists.isPresent())
        throw new IllegalStateException("You have configured to use a folder " + folder + ", which seems to be not a folder job");

      return folderJobIfExists.get();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
