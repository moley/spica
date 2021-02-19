package org.spica.server.demodata;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.spica.server.software.model.SoftwareInfo;
import org.spica.server.software.service.SoftwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DemoDataCreator {

  @Autowired
  private SoftwareService softwareService;

  public void createSoftware () {
    log.info("Create demodata software");

    SoftwareInfo softwareInfoSpica = new SoftwareInfo().name("Spica").description("Extensible, project-oriented development and communication platform");
    softwareInfoSpica.addChildrenItem(new SoftwareInfo().name("Spica-Server").description("Springboot Server, which provides functionality to improve automation and interactions in development teams"));
    softwareInfoSpica.addChildrenItem(new SoftwareInfo().name("Spica-JavaFXClient").description("JavaFX Client providing integration between development environment and the spica server"));

    SoftwareInfo softwareInfoSpicaCLI = new SoftwareInfo().name("Spica-CLI").description("Commandline Interface providing integration between the console and the spica server");
    SoftwareInfo softwareInfoSpicaJenkinsIntegration = new SoftwareInfo().name("Spica Jenkins Integration").description("Provides automation of Jenkins");
    SoftwareInfo softwareInfoSpicaBitbucketIntegration = new SoftwareInfo().name("Spica Bitbucket Integration").description("Provides automation of Bitbucket");
    SoftwareInfo softwareInfoSpicaXMPPIntegration = new SoftwareInfo().name("Spica XMPP Integration").description("Provides automation of XMPP");
    SoftwareInfo softwareInfoSpicaMailIntegration = new SoftwareInfo().name("Spica Mail Integration").description("Provides automation of Mail");
    softwareInfoSpicaCLI.addChildrenItem(softwareInfoSpicaJenkinsIntegration);
    softwareInfoSpicaCLI.addChildrenItem(softwareInfoSpicaBitbucketIntegration);
    softwareInfoSpicaCLI.addChildrenItem(softwareInfoSpicaXMPPIntegration);
    softwareInfoSpicaCLI.addChildrenItem(softwareInfoSpicaMailIntegration);
    softwareInfoSpica.addChildrenItem(softwareInfoSpicaCLI);

    SoftwareInfo softwareInfoJenkins = new SoftwareInfo().name("Jenkins").description("Jenkins is a cool buildserver");
    SoftwareInfo softwareInfoBitbucket = new SoftwareInfo().name("Bitbucket").description("Bitbucket is a version control service");
    SoftwareInfo softwareInfoXmpp = new SoftwareInfo().name("XMPPServer").description("XMPP servers provide chats");
    SoftwareInfo softwareInfoMail = new SoftwareInfo().name("MailServer").description("Mail servers provide mails");

    softwareService.setSoftware(Arrays.asList(softwareInfoSpica, softwareInfoJenkins, softwareInfoBitbucket, softwareInfoXmpp, softwareInfoMail));

  }

  public void create() {
    createSoftware();
  }
}
