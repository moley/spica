package org.spica.devclient.model;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.SpicaProperties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.Serializable;
import java.io.StringReader;


public class ModelCacheService implements Serializable{

  private static final Logger LOGGER = LoggerFactory.getLogger(ModelCacheService.class);


  private File spicaHome = SpicaProperties.getSpicaHome();
  private File configFile;


  private static ModelCache currentConfiguration;

  public void setConfigFile (final File configFile) {
    this.configFile = configFile;
  }

  public File getConfigFile (){
    if (configFile == null) {
      configFile = new File(spicaHome, "config.xml");
      LOGGER.info("configFile = " + configFile.getAbsolutePath());
    }
    return configFile;

  }

  public ModelCache get() {
    if (currentConfiguration != null)
      return currentConfiguration;

    JAXBContext jc = null;
    File configFile = getConfigFile();

    try {
      jc = JAXBContext.newInstance(ModelCache.class);
      Unmarshaller unmarshaller = jc.createUnmarshaller();

      if (configFile.exists()) {
        LOGGER.info("Load configuration from " + configFile.getAbsolutePath());
        currentConfiguration = (ModelCache) unmarshaller.unmarshal(configFile);
        currentConfiguration.setCurrentFile(configFile);
      }
      else {
        LOGGER.info("Create new configuration because " + configFile.getAbsolutePath() + " does not exist");
        currentConfiguration = new ModelCache();
        set(currentConfiguration);
      }
    } catch (Exception e) {
      LOGGER.error("Error loading configurations from " + configFile.getAbsolutePath() + ":" + e.getLocalizedMessage(), e);
      currentConfiguration = new ModelCache();
      currentConfiguration.setCurrentFile(configFile);
    }



    return currentConfiguration;

  }

  public boolean isValid (final String configString) {
    try {
      JAXBContext jc = JAXBContext.newInstance(ModelCache.class);
      Unmarshaller unmarshaller = jc.createUnmarshaller();
      unmarshaller.unmarshal(new StringReader(configString));
      return true;
    } catch (Exception e) {
      LOGGER.info("String " + configString + " is not valid", e);
      return false;
    }
  }


  public void set(ModelCache configuration) {
    JAXBContext jc = null;
    try {
      jc = JAXBContext.newInstance(ModelCache.class);
      Marshaller marshaller = jc.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

      File toFile = configuration.getCurrentFile() != null ? configuration.getCurrentFile(): getConfigFile();
      if (toFile == null)
        throw new IllegalStateException("configuration file not set before saving ");

      LOGGER.info("Save configuration " + toFile.getAbsolutePath());

      if (!toFile.getParentFile().exists())
        toFile.getParentFile().mkdirs();

      marshaller.marshal(configuration, toFile);
    } catch (JAXBException e) {
      throw new IllegalStateException(e);
    }
  }

  public void close () {
    currentConfiguration = null;
  }


}
