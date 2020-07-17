package org.spica.javaclient.services;

import org.spica.commons.services.bitbucket.BitbucketService;
import org.spica.commons.services.download.DownloadService;
import org.spica.commons.services.jenkins.JenkinsService;
import org.spica.commons.services.jira.JiraService;
import org.spica.commons.services.mail.MailService;

public class Services {

  private ModelCacheService modelCacheService = new ModelCacheService();

  private DownloadService downloadService = new DownloadService();

  private MailService mailService = new MailService();


  private JenkinsService jenkinsService = new JenkinsService();

  private JiraService jiraService = new JiraService();

  private BitbucketService bitbucketService = new BitbucketService();

  public Services() {
    modelCacheService.migrateOnDemand();
  }

  /**
   * get the model cache service to have access to the model or save the model
   * @return model cache service
   */
  public ModelCacheService getModelCacheService() {
    return modelCacheService;
  }

  /**
   * get the download service to be able to e.g check if a url has changed or download things
   * @return download service
   */
  public DownloadService getDownloadService() {
    return downloadService;
  }

  /**
   * get the mail service to be able to e.g.send mails
   * @return mail service
   */
  public MailService getMailService() {
    return mailService;
  }

  /**
   * get the jenkins service to be able to e.g. start buildjobs
   * This service must be logged in by client calling  #{@link JenkinsService#connectToServer()} ()}
   * @return jenkins service
   */
  public JenkinsService getJenkinsService () {
    return jenkinsService;
  }

  /**
   * get the jira service to be able to e.g. handle issues in jira directly
   * This service must be logged in by client calling  #{@link JiraService#connectToServer()}
   * @return jira service
   */
  public JiraService getJiraService () {
    return jiraService;
  }

  /**
   * get the bitbucket service to be able to e.g. read projects, modules and branches
   * This service must be logged in by client calling  #{@link BitbucketService#connectToServer()} ()}
   * @return bitbucket service
   */
  public BitbucketService getBitbucketService () {
    return bitbucketService;
  }


}
