package org.spica.javaclient;

import java.util.ArrayList;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.services.ModelCacheService;

public class AdminTester {

  public static void main(String[] args) {

    //Remove message containers
    ModelCacheService modelCacheService = new ModelCacheService();
    Model model = modelCacheService.get();
    model.setSelectedMessageContainer(null);
    model.setMessagecontainerInfos(new ArrayList<>());

    modelCacheService.set(model, "Remove all messagecontainers");
  }

}
