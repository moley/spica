package org.spica.javaclient.services;

public class Services {


  private ModelCacheService modelCacheService = new ModelCacheService();

  private DownloadService downloadService = new DownloadService();

  public ModelCacheService getModelCacheService() {
    return modelCacheService;
  }

  public DownloadService getDownloadService() {
    return downloadService;
  }
}
