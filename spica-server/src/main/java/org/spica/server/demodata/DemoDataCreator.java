package org.spica.server.demodata;

import java.util.Arrays;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.KeyValue;
import org.spica.commons.SpicaProperties;
import org.spica.server.software.model.IdAndDisplaynameInfo;
import org.spica.server.software.model.SoftwareInfo;
import org.spica.server.software.service.SoftwareMapper;
import org.spica.server.software.service.SoftwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DemoDataCreator {

  @Autowired
  private SoftwareService softwareService;

  private SoftwareMapper softwareMapper = new SoftwareMapper();

  private SpicaProperties spicaProperties = new SpicaProperties();

  private IdAndDisplaynameInfo getIdAndDisplaynameInfo (final String key) {
    KeyValue keyValuePair = spicaProperties.getKeyValuePair(key);
    return softwareMapper.toIdAndDisplaynameInfo(keyValuePair);
  }

  public void createSoftware () {
    log.info("Create demodata software");

    SoftwareInfo softwareInfoSpica = new SoftwareInfo().id(UUID.randomUUID().toString()).name("Spica").description("Extensible, project-oriented development and communication platform");
    softwareInfoSpica = softwareInfoSpica.type(getIdAndDisplaynameInfo("spica.software.type.system")).group(getIdAndDisplaynameInfo("spica.software.group.development"));
    softwareInfoSpica = softwareInfoSpica.state(getIdAndDisplaynameInfo("spica.software.state.worked"));
    softwareInfoSpica = softwareInfoSpica.technologies(Arrays.asList("Java", "Gradle"));

    SoftwareInfo softwareInfoSpicaServer = new SoftwareInfo().id(UUID.randomUUID().toString()).parentId(softwareInfoSpica.getId()).name("Spica-Server").description("Springboot Server, which provides functionality to improve automation and interactions in development teams");
    softwareInfoSpicaServer = softwareInfoSpicaServer.technologies(Arrays.asList("SpringBoot", "REST"));
    SoftwareInfo softwareInfoSpicaCli = new SoftwareInfo().id(UUID.randomUUID().toString()).parentId(softwareInfoSpica.getId()).name("Spica-CLI").description("Commandline Interface providing integration between the console and the spica server");
    softwareInfoSpicaCli = softwareInfoSpicaCli.technologies(Arrays.asList("ConsoleUI"));

    SoftwareInfo softwareInfoSpicaJenkinsIntegration = new SoftwareInfo().id(UUID.randomUUID().toString()).parentId(softwareInfoSpicaCli.getId()).name("Spica Jenkins Integration").description("Provides automation of Jenkins");
    SoftwareInfo softwareInfoSpicaBitbucketIntegration = new SoftwareInfo().id(UUID.randomUUID().toString()).parentId(softwareInfoSpicaCli.getId()).name("Spica Bitbucket Integration").description("Provides automation of Bitbucket");
    SoftwareInfo softwareInfoSpicaXMPPIntegration = new SoftwareInfo().id(UUID.randomUUID().toString()).parentId(softwareInfoSpicaCli.getId()).name("Spica XMPP Integration").description("Provides automation of XMPP");
    SoftwareInfo softwareInfoSpicaMailIntegration = new SoftwareInfo().id(UUID.randomUUID().toString()).parentId(softwareInfoSpicaCli.getId()).name("Spica Mail Integration").description("Provides automation of Mail");
    softwareInfoSpicaCli.addChildrenItem(softwareInfoSpicaJenkinsIntegration);
    softwareInfoSpicaCli.addChildrenItem(softwareInfoSpicaBitbucketIntegration);
    softwareInfoSpicaCli.addChildrenItem(softwareInfoSpicaXMPPIntegration);
    softwareInfoSpicaCli.addChildrenItem(softwareInfoSpicaMailIntegration);

    SoftwareInfo softwareInfoJenkins = new SoftwareInfo().id(UUID.randomUUID().toString()).name("Jenkins").description("Jenkins is a cool buildserver");
    SoftwareInfo softwareInfoBitbucket = new SoftwareInfo().id(UUID.randomUUID().toString()).name("Bitbucket").description("Bitbucket is a version control service");
    SoftwareInfo softwareInfoXmpp = new SoftwareInfo().id(UUID.randomUUID().toString()).name("XMPPServer").description("XMPP servers provide chats");
    SoftwareInfo softwareInfoMail = new SoftwareInfo().id(UUID.randomUUID().toString()).name("MailServer").description("Mail servers provide mails");

    softwareInfoSpica.addChildrenItem(softwareInfoSpicaServer);
    softwareInfoSpica.addChildrenItem(softwareInfoSpicaCli);


    softwareService.setSoftwareList(Arrays.asList(softwareInfoSpica, softwareInfoJenkins, softwareInfoBitbucket, softwareInfoXmpp, softwareInfoMail));

  }

  public void create() {
    createSoftware();
  }
}
