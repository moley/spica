package org.spica.javaclient.services;

public class Services {

  private ModelCacheService modelCacheService = new ModelCacheService();

  private DownloadService downloadService = new DownloadService();

  private MailService mailService = new MailService();

  private JenkinsService jenkinsService = new JenkinsService();

  private BitbucketService bitbucketService = new BitbucketService();

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

  public BitbucketService getBitbucketService () {
    return bitbucketService;
  }
}
