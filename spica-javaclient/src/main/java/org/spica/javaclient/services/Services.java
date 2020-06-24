package org.spica.javaclient.services;

import org.spica.commons.services.bitbucket.BitbucketService;
import org.spica.commons.services.download.DownloadService;
import org.spica.commons.services.jenkins.JenkinsService;
import org.spica.commons.services.jira.JiraService;
import org.spica.commons.services.mail.MailService;
import org.spica.javaclient.api.ProjectApi;
import org.spica.javaclient.api.TaskApi;
import org.spica.javaclient.api.UserApi;

public class Services {

  private ModelCacheService modelCacheService = new ModelCacheService();

  private DownloadService downloadService = new DownloadService();

  private MailService mailService = new MailService();

  private JenkinsService jenkinsService = new JenkinsService();

  private JiraService jiraService = new JiraService();

  private BitbucketService bitbucketService = new BitbucketService();

  private TaskApi taskApi = new TaskApi();

  private UserApi userApi = new UserApi();

  private ProjectApi projectApi = new ProjectApi();

  public Services() {
    modelCacheService.migrateOnDemand();
  }

  public ModelCacheService getModelCacheService() {
    return modelCacheService;
  }

  public DownloadService getDownloadService() {
    return downloadService;
  }

  public MailService getMailService() {
    return mailService;
  }

  public JenkinsService getJenkinsService () {
    return jenkinsService;
  }

  public JiraService getJiraService () {
    return jiraService;
  }

  public BitbucketService getBitbucketService () {
    return bitbucketService;
  }

  public TaskApi getTaskApi() {
    return taskApi;
  }

  public ProjectApi getProjectApi() {
    return projectApi;
  }

  public UserApi getUserApi () {
    return userApi;
  }
}
