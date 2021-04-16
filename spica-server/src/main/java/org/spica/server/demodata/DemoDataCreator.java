package org.spica.server.demodata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.spica.commons.KeyValue;
import org.spica.commons.SpicaProperties;
import org.spica.commons.filestore.FilestoreService;
import org.spica.server.software.db.SoftwareMetricsRepository;
import org.spica.server.software.db.SoftwareRepository;
import org.spica.server.software.domain.Software;
import org.spica.server.software.domain.SoftwareMetrics;
import org.spica.server.software.model.IdAndDisplaynameInfo;
import org.spica.server.software.model.RelationInfo;
import org.spica.server.software.model.SoftwareInfo;
import org.spica.server.software.service.SoftwareMapper;
import org.spica.server.software.service.SoftwareMetricsProvider;
import org.spica.server.software.service.SoftwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DemoDataCreator {

  @Autowired
  private SoftwareService softwareService;

  private SoftwareMapper softwareMapper = new SoftwareMapper();


  private FilestoreService filestoreService = new FilestoreService();

  private SoftwareMetricsProvider softwareMetricsProvider = new SoftwareMetricsProvider();

  @Autowired
  private SoftwareMetricsRepository softwareMetricsRepository;

  @Autowired
  private SoftwareRepository softwareRepository;

  public final static String TYPE_REST = "spica.software.type.rest";
  public final static  String TYPE_INTERFACE = "spica.software.type.rest";
  public final static  String TYPE_SYSTEM = "spica.software.type.system";
  public final static  String TYPE_WEBAPP = "spica.software.type.webapp";

  public final static  String STATE_WORKED = "spica.software.state.worked";
  public final static  String STATE_HOLD = "spica.software.state.hold";
  public final static  String STATE_PRODUCTIVE = "spica.software.state.productive";

  public final static  String DEPLOYMENT_DATACENTER = "spica.software.type.system";
  public final static  String DEPLOYMENT_CLOUD = "spica.software.type.cloud";
  public final static  String DEPLOYMENT_MOBILE = "spica.software.type.mobile";
  public final static  String DEPLOYMENT_LOCAL = "spica.software.type.local";

  public final static  String GROUP_INFOSYSTEM = "spica.software.group.infosystem";
  public final static  String GROUP_AUTOMATION = "spica.software.group.automation";
  public final static  String GROUP_COMMUNICATION = "spica.software.group.communication";
  public final static  String GROUP_DEVELOPMENT = "spica.software.group.development";


  private IdAndDisplaynameInfo getIdAndDisplaynameInfoFromConfiguration(final SpicaProperties spicaProperties, final String key) {

    KeyValue keyValuePair = spicaProperties.getKeyValuePair(key);
    return softwareMapper.toIdAndDisplaynameInfo(keyValuePair);
  }

  private IdAndDisplaynameInfo getIdAndDisplaynameInfoFromString (final String string) {
    return new IdAndDisplaynameInfo().displayname(string).id(string);
  }

  public void createProperties () {

    Properties customProperties = new Properties();
    customProperties.setProperty(TYPE_WEBAPP, "Web Application");
    customProperties.setProperty("spica.software.type.mobapp", "Mobile Application");
    customProperties.setProperty("spica.software.type.standaloneapp", "Standalone Application (without GUI)");
    customProperties.setProperty("spica.software.type.webgui", "Web GUI");
    customProperties.setProperty(TYPE_INTERFACE, "Generic Interface");
    customProperties.setProperty(TYPE_REST, "REST Interface");
    customProperties.setProperty("spica.software.type.soap", "SOAP Interface");
    customProperties.setProperty("spica.software.type.webservice", "Webservice");
    customProperties.setProperty("spica.software.type.data", "Data");
    customProperties.setProperty(TYPE_SYSTEM, "System");

    customProperties.setProperty(DEPLOYMENT_DATACENTER, "Datacenter");
    customProperties.setProperty(DEPLOYMENT_CLOUD, "Cloud");
    customProperties.setProperty(DEPLOYMENT_LOCAL, "Local");
    customProperties.setProperty(DEPLOYMENT_MOBILE, "Mobilephone");

    customProperties.setProperty("spica.software.state.idea","Idea");
    customProperties.setProperty("spica.software.state.planned", "Planned ");
    customProperties.setProperty(STATE_WORKED, "Worked on");
    customProperties.setProperty(STATE_PRODUCTIVE, "Productive");
    customProperties.setProperty("spica.software.state.betatest", "Betatest");
    customProperties.setProperty(STATE_HOLD, "On Hold");

    customProperties.setProperty("spica.software.relationship.uses", "Uses");
    customProperties.setProperty("spica.software.relationship.owns", "Owns");

    customProperties.setProperty(GROUP_INFOSYSTEM, "InfoSystem");
    customProperties.setProperty(GROUP_AUTOMATION, "Automation");
    customProperties.setProperty(GROUP_COMMUNICATION, "Communication");
    customProperties.setProperty(GROUP_DEVELOPMENT, "Development");

    customProperties.setProperty("spica.software.role.developer", "Developer");

    try {
      Properties properties = new Properties();
      File propertiesfile = new File(".spica/spica.properties");
      properties.setProperty("spica.cli.serverurl", "http://localhost:8765/api");
      properties.setProperty("spica.cli.username", "mm");
      properties.setProperty("spica.cli.password", "mm");
      properties.setProperty("spica.cli.usermail", "marc.mayer@spica.org");
      properties.setProperty("spica.authentication.provider", "org.spica.server.security.DefaultAuthenticationProvider");
      properties.store(new FileOutputStream(propertiesfile), "Autogenerated by spica tests, do not change");

      SpicaProperties.close(); //force reload

    } catch (IOException e) {
      throw new IllegalStateException(e);
    }



    SpicaProperties spicaProperties = new SpicaProperties();
    spicaProperties.saveCustomProperties(customProperties);
  }

  public void createSoftware () {
    log.info("Create demodata software");


    SpicaProperties spicaProperties = new SpicaProperties();

    SoftwareInfo softwareInfoSpica = new SoftwareInfo().id("SPICA").name("Spica").description("Extensible, project-oriented development and communication platform");
    softwareInfoSpica = softwareInfoSpica.type(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, TYPE_SYSTEM)).group(
        getIdAndDisplaynameInfoFromConfiguration(spicaProperties, GROUP_DEVELOPMENT));
    softwareInfoSpica = softwareInfoSpica.state(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, STATE_WORKED));
    softwareInfoSpica = softwareInfoSpica.technologies(Arrays.asList(getIdAndDisplaynameInfoFromString("Java"), getIdAndDisplaynameInfoFromString("Gradle")));

    SoftwareInfo softwareInfoSpicaServer = new SoftwareInfo().parentId(softwareInfoSpica.getId()).id("SPICASERVER").name("Spica-Server").description("Springboot Server, which provides functionality to improve automation and interactions in development teams");
    softwareInfoSpicaServer = softwareInfoSpicaServer.technologies(Arrays.asList(getIdAndDisplaynameInfoFromString("SpringBoot"), getIdAndDisplaynameInfoFromString("REST")));
    softwareInfoSpicaServer = softwareInfoSpicaServer.state(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, STATE_WORKED));

    softwareInfoSpicaServer = softwareInfoSpicaServer.deployment(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, DEPLOYMENT_DATACENTER));

    SoftwareInfo softwareInfoSpicaCli = new SoftwareInfo().parentId(softwareInfoSpica.getId()).id("SPICACLI").name("Spica-CLI").description("Commandline Interface providing integration between the console and the spica server");
    softwareInfoSpicaCli = softwareInfoSpicaCli.technologies(Arrays.asList(getIdAndDisplaynameInfoFromString("ConsoleUI")));
    softwareInfoSpicaCli = softwareInfoSpicaCli.deployment(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, DEPLOYMENT_LOCAL));
    softwareInfoSpicaCli = softwareInfoSpicaCli.state(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, STATE_WORKED));

    SoftwareInfo softwareInfoSpicaJenkinsIntegration = new SoftwareInfo().id("SPICAJENKINS").parentId(softwareInfoSpicaCli.getId()).name("Spica Jenkins Integration").description("Provides automation of Jenkins");
    softwareInfoSpicaJenkinsIntegration = softwareInfoSpicaJenkinsIntegration.type(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, TYPE_REST));
    softwareInfoSpicaJenkinsIntegration = softwareInfoSpicaJenkinsIntegration.state(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, STATE_PRODUCTIVE));


    SoftwareInfo softwareInfoSpicaBitbucketIntegration = new SoftwareInfo().id("SPICABITBUCKET").parentId(softwareInfoSpicaCli.getId()).name("Spica Bitbucket Integration").description("Provides automation of Bitbucket");
    softwareInfoSpicaBitbucketIntegration = softwareInfoSpicaBitbucketIntegration.type(getIdAndDisplaynameInfoFromConfiguration(spicaProperties,TYPE_REST));
    softwareInfoSpicaBitbucketIntegration = softwareInfoSpicaBitbucketIntegration.state(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, STATE_PRODUCTIVE));

    SoftwareInfo softwareInfoSpicaXMPPIntegration = new SoftwareInfo().id("SPICAXMPP").parentId(softwareInfoSpicaCli.getId()).name("Spica XMPP Integration").description("Provides automation of XMPP");
    softwareInfoSpicaXMPPIntegration = softwareInfoSpicaXMPPIntegration.type(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, TYPE_INTERFACE));
    softwareInfoSpicaXMPPIntegration = softwareInfoSpicaXMPPIntegration.state(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, STATE_PRODUCTIVE));

    SoftwareInfo softwareInfoSpicaMailIntegration = new SoftwareInfo().id("SPICAMAIL").parentId(softwareInfoSpicaCli.getId()).name("Spica Mail Integration").description("Provides automation of Mail");
    softwareInfoSpicaMailIntegration = softwareInfoSpicaMailIntegration.type(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, TYPE_INTERFACE));
    softwareInfoSpicaMailIntegration = softwareInfoSpicaMailIntegration.state(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, STATE_PRODUCTIVE));

    softwareInfoSpicaCli.addChildrenItem(softwareInfoSpicaJenkinsIntegration);
    softwareInfoSpicaCli.addChildrenItem(softwareInfoSpicaBitbucketIntegration);
    softwareInfoSpicaCli.addChildrenItem(softwareInfoSpicaXMPPIntegration);
    softwareInfoSpicaCli.addChildrenItem(softwareInfoSpicaMailIntegration);

    SoftwareInfo softwareInfoJenkins = new SoftwareInfo().id("JENKINS").name("Jenkins").description("Jenkins is a cool buildserver");
    softwareInfoJenkins = softwareInfoJenkins.type(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, TYPE_SYSTEM)).group(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, "spica.software.group.automation"));
    softwareInfoJenkins = softwareInfoJenkins.deployment(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, DEPLOYMENT_DATACENTER));
    softwareInfoJenkins = softwareInfoJenkins.state(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, STATE_PRODUCTIVE));

    SoftwareInfo softwareInfoBitbucket = new SoftwareInfo().id("BITBUCKET").name("Bitbucket").description("Bitbucket is a version control service");
    softwareInfoBitbucket = softwareInfoBitbucket.type(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, TYPE_SYSTEM)).group(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, "spica.software.group.development"));
    softwareInfoBitbucket = softwareInfoBitbucket.deployment(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, DEPLOYMENT_DATACENTER));
    softwareInfoBitbucket = softwareInfoBitbucket.state(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, STATE_PRODUCTIVE));

    SoftwareInfo softwareInfoXmpp = new SoftwareInfo().id("XMPPSERVER").name("XMPPServer").description("XMPP servers provide chats");
    softwareInfoXmpp = softwareInfoXmpp.type(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, TYPE_SYSTEM)).group(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, "spica.software.group.communication"));
    softwareInfoXmpp = softwareInfoXmpp.deployment(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, DEPLOYMENT_DATACENTER));
    softwareInfoXmpp = softwareInfoXmpp.state(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, STATE_PRODUCTIVE));

    SoftwareInfo softwareInfoMail = new SoftwareInfo().id("MAILSERVER").name("MailServer").description("Mail servers provide mails");
    softwareInfoMail = softwareInfoMail.type(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, TYPE_SYSTEM)).group(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, "spica.software.group.communication"));
    softwareInfoMail = softwareInfoMail.deployment(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, DEPLOYMENT_CLOUD));
    softwareInfoMail = softwareInfoMail.state(getIdAndDisplaynameInfoFromConfiguration(spicaProperties, STATE_PRODUCTIVE));

    softwareInfoSpica.addChildrenItem(softwareInfoSpicaServer);
    softwareInfoSpica.addChildrenItem(softwareInfoSpicaCli);

    softwareService.setSoftwareList(Arrays.asList(softwareInfoSpica, softwareInfoJenkins, softwareInfoBitbucket, softwareInfoXmpp, softwareInfoMail));

    RelationInfo spicaJenkinsRelation = new RelationInfo().id("SPICAJENKINS").source(softwareInfoSpicaJenkinsIntegration).target(softwareInfoJenkins);
    RelationInfo spicaBitbucketRelation = new RelationInfo().id("SPICABITBUCKET").source(softwareInfoSpicaBitbucketIntegration).target(softwareInfoBitbucket);
    RelationInfo spicaXMPPServerRelation = new RelationInfo().id("SPICAXMPP").source(softwareInfoSpicaXMPPIntegration).target(softwareInfoXmpp);
    RelationInfo spicaMailServerRelation = new RelationInfo().id("SPICAMAIL").source(softwareInfoSpicaMailIntegration).target(softwareInfoMail);
    softwareService.setRelationList(Arrays.asList(spicaBitbucketRelation, spicaJenkinsRelation, spicaXMPPServerRelation, spicaMailServerRelation));

    File iconPath = new File ("src/test/resources");
    if (! iconPath.exists())
      throw new IllegalStateException("Icon path " + iconPath.getAbsolutePath() + " does not exist");

    File bitbucketIcon = new File (iconPath, "bitbucket.png");
    File jenkinsIcon = new File (iconPath, "cool-jenkins.png");
    File mailIcon = new File (iconPath, "mail.png");
    File xmppIcon = new File (iconPath, "xmpp.png");

    filestoreService.saveObjectItem(softwareInfoJenkins, FilestoreService.CONTEXT_SCREENSHOT, jenkinsIcon);
    filestoreService.saveObjectItem(softwareInfoBitbucket, FilestoreService.CONTEXT_SCREENSHOT, bitbucketIcon);
    filestoreService.saveObjectItem(softwareInfoMail, FilestoreService.CONTEXT_SCREENSHOT, mailIcon);
    filestoreService.saveObjectItem(softwareInfoXmpp, FilestoreService.CONTEXT_SCREENSHOT, xmppIcon);

    //old metrics every
    Collection<Software> softwareInfoList = softwareRepository.findByParentIdIsNull();

    LocalDate localDate = LocalDate.now();
    do {

      log.info("Software: " + softwareInfoList.size());

      softwareMetricsProvider.create(softwareInfoList,localDate);
      localDate = localDate.minus(1, ChronoUnit.MONTHS);
      SoftwareMetrics softwareMetrics = softwareMetricsProvider.create(softwareInfoList, localDate);
      softwareMetricsRepository.save(softwareMetrics);
      softwareInfoList.remove(softwareInfoList.iterator().next());

    }  while (softwareInfoList.size() > 0);



  }

  public void create() {
    File spicaPath = new File(".spica");
    FileUtils.deleteQuietly(spicaPath);
    spicaPath.mkdirs();

    createProperties();
    createSoftware();
  }
}
