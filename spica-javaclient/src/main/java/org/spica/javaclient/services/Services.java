package org.spica.javaclient.services;

import lombok.Data;
import org.spica.commons.filestore.FilestoreService;
import org.spica.commons.services.bitbucket.BitbucketService;
import org.spica.commons.services.download.DownloadService;
import org.spica.commons.services.jenkins.JenkinsService;
import org.spica.commons.services.jira.JiraService;
import org.spica.commons.services.mail.MailService;
import org.spica.commons.services.remote.SshService;
import org.spica.commons.xmpp.XMPPAdapter;
import org.spica.javaclient.mail.MailImporter;

@Data
public class Services {

  /**
   * get the model cache service to have access to the model or save the model
   * @return model cache service
   */
  private ModelCacheService modelCacheService = new ModelCacheService();

  /**
   * get the download service to be able to e.g check if a url has changed or download things
   * @return download service
   */
  private DownloadService downloadService = new DownloadService();

  /**
   * get the mail service to be able to e.g.send mails
   * @return mail service
   */
  private MailService mailService = new MailService();

  private SshService sshService = new SshService();


  /**
   * get the XMPP adapter to be able to chat
   * @return xmpp adapter
   */
  private XMPPAdapter xmppAdapter = new XMPPAdapter();

  /**
   * get the jenkins service to be able to e.g. start buildjobs
   * This service must be logged in by client calling  #{@link JenkinsService#connectToServer()} ()}
   * @return jenkins service
   */
  private JenkinsService jenkinsService = new JenkinsService();

  /**
   * get the jira service to be able to e.g. handle issues in jira directly
   * This service must be logged in by client calling  #{@link JiraService#connectToServer()}
   * @return jira service
   */
  private JiraService jiraService = new JiraService();

  /**
   * get the bitbucket service to be able to e.g. read projects, modules and branches
   * This service must be logged in by client calling  #{@link BitbucketService#connectToServer()} ()}
   * @return bitbucket service
   */
  private BitbucketService bitbucketService = new BitbucketService();

  private MailImporter mailImporter = new MailImporter();

  private FilestoreService filestoreService = new FilestoreService();

  public Services() {
    modelCacheService.migrateOnDemand();
  }


}
