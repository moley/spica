package org.spica.javaclient.services;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.DashboardItemType;
import org.spica.commons.SpicaProperties;
import org.spica.javaclient.model.DashboardItemInfo;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.Model;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.model.TaskInfo;

@Slf4j
public class ModelCacheService implements Serializable{

  private static final Logger LOGGER = LoggerFactory.getLogger(ModelCacheService.class);


  private File spicaHome = SpicaProperties.getSpicaHome();
  private File configFile;


  private static Model currentConfiguration;

  public void setConfigFile (final File configFile) {
    this.configFile = configFile;
  }

  public static boolean isDefault () {
    String userHome = System.getProperty("user.home") + "/.spica";
    String configFile = System.getProperty("spica.configfile");
    boolean isDefault = configFile.startsWith(userHome);
    LOGGER.info ("isDefault " + isDefault);
    return isDefault;
  }

  public File getConfigFile (){
    if (configFile == null) {
      configFile = new File(spicaHome, "config.xml");
      LOGGER.info("configFile = " + configFile.getAbsolutePath());
    }

    System.setProperty("spica.configfile", configFile.getAbsolutePath());
    return configFile;

  }

  public void migrateOnDemand () {
    Model model = get();
    for (EventInfo nextEvent: model.getEventInfosReal()) {

      //create an event on dashboard if does not exist yet
      if (model.findDashboardItemInfo(DashboardItemType.EVENT, nextEvent.getId()) == null) {
        DashboardItemInfo dashboardItemInfo = new DashboardItemInfo();
        dashboardItemInfo.setId(UUID.randomUUID().toString());
        dashboardItemInfo.setCreated(nextEvent.getStart());
        dashboardItemInfo.setDescription(nextEvent.getName());
        dashboardItemInfo.setItemReference(nextEvent.getId());
        dashboardItemInfo.setItemType(DashboardItemType.EVENT.name());
        model.getDashboardItemInfos().add(dashboardItemInfo);
      }
    }





    for (TaskInfo nextTaskInfo: model.getTaskInfos()) {
      if (nextTaskInfo.getId() == null)
        nextTaskInfo.setId(UUID.randomUUID().toString());
    }

    //TODO move to server
    if (model.getProjectInfos() == null) {
      model.setProjectInfos(new ArrayList<>());
    }

    if (model.findProjectInfoById(Model.DEFAULTTASK_PRIVATE) == null) {
      log.info("Creating default task " + Model.DEFAULTTASK_PRIVATE);
      ProjectInfo projectPrivate = new ProjectInfo();
      projectPrivate.setId(Model.DEFAULTTASK_PRIVATE);
      projectPrivate.setName(Model.DEFAULTTASK_PRIVATE);
      projectPrivate.setColor("0x80b380ff");
      model.getProjectInfos().add(projectPrivate);
      set(model, "Create default task " + Model.DEFAULTTASK_PRIVATE);
    }
    else
      log.info("Default task " + Model.DEFAULTTASK_PRIVATE + " already exists");

    if (model.findProjectInfoById(Model.DEFAULTTASK_WORK) == null) {
      log.info("Creating default task " + Model.DEFAULTTASK_WORK);
      ProjectInfo projectWork = new ProjectInfo();
      projectWork.setId(Model.DEFAULTTASK_WORK);
      projectWork.setName(Model.DEFAULTTASK_WORK);
      projectWork.setColor("0x8099ffff");
      model.getProjectInfos().add(projectWork);
      set(model, "Create default task " + Model.DEFAULTTASK_WORK);
    }
    else
      log.info("Default task " + Model.DEFAULTTASK_WORK + " already exists");

    //TODO temp
    //List<DashboardItemInfo> dashboardItemInfos = new ArrayList<DashboardItemInfo>();
    //for (DashboardItemInfo nextInfo: model.getDashboardItemInfos()) {
    //  if (nextInfo.getItemType().equals(DashboardItemType.MAIL.toString()))
    //    dashboardItemInfos.add(nextInfo);
    // }
    //model.getDashboardItemInfos().removeAll(dashboardItemInfos);
    //model.getMessagecontainerInfos().clear();

    closeEventDashboardsWhenEventIsClosed();
    forceId();
  }

  public void forceId () {
    Model model = get();
    for (DashboardItemInfo nextDashboardItem : model.getDashboardItemInfos()) {
      if (nextDashboardItem.getId() == null)
        nextDashboardItem.setId(UUID.randomUUID().toString());
    }
  }

  public void closeEventDashboardsWhenEventIsClosed () {
    Model model = get();
    for (DashboardItemInfo nextDashboardItem: model.getDashboardItemInfos()) {
      if (nextDashboardItem.getItemType().equals(DashboardItemType.EVENT.name())) {
        EventInfo eventInfoRealById = model.findEventInfoRealById(nextDashboardItem.getItemReference());
        nextDashboardItem.setOpen(eventInfoRealById.getStop() == null);
      }
    }

  }


  public Model get() {
    if (currentConfiguration != null)
      return currentConfiguration;

    JAXBContext jc = null;
    File configFile = getConfigFile();

    try {
      jc = JAXBContext.newInstance(Model.class);
      Unmarshaller unmarshaller = jc.createUnmarshaller();

      if (configFile.exists()) {
        LOGGER.info("Load configuration from " + configFile.getAbsolutePath());
        currentConfiguration = (Model) unmarshaller.unmarshal(configFile);
      }
      else {
        LOGGER.info("Create new configuration because " + configFile.getAbsolutePath() + " does not exist");
        currentConfiguration = new Model();
        set(currentConfiguration, "Creation");
      }
    } catch (Exception e) {
      LOGGER.error("Error loading configurations from " + configFile.getAbsolutePath() + ":" + e.getLocalizedMessage(), e);
      currentConfiguration = new Model();
    }



    return currentConfiguration;

  }

  public boolean isValid (final String configString) {
    try {
      JAXBContext jc = JAXBContext.newInstance(Model.class);
      Unmarshaller unmarshaller = jc.createUnmarshaller();
      unmarshaller.unmarshal(new StringReader(configString));
      return true;
    } catch (Exception e) {
      LOGGER.info("String " + configString + " is not valid", e);
      return false;
    }
  }


  public void set(Model configuration, final String action) {
    JAXBContext jc = null;
    try {
      jc = JAXBContext.newInstance(Model.class);
      Marshaller marshaller = jc.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

      File toFile = getConfigFile();
      if (toFile == null)
        throw new IllegalStateException("configuration file not set before saving ");

      LOGGER.info("Save configuration " + toFile.getAbsolutePath() + "(" + action + ")");
      toFile = toFile.getAbsoluteFile();

      if (!toFile.getParentFile().exists())
        toFile.getParentFile().mkdirs();

      if (toFile.exists()) {
        File savedXml = new File (toFile.getParentFile(), "config_" + new Date() + ".xml");
        File savedMetainf = new File (toFile.getParentFile(), "config_" + new Date() + ".metainf");
        FileUtils.copyFile(toFile, savedXml);
        FileUtils.write(savedMetainf, "Last action: " + action, Charset.defaultCharset());
      }

      marshaller.marshal(configuration, toFile);
    } catch (JAXBException e) {
      throw new IllegalStateException(e);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public void close () {
    currentConfiguration = null;
  }


}
