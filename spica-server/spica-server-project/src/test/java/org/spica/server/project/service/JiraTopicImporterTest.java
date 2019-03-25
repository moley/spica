package org.spica.server.project.service;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(value = {SpringExtension.class})
@DataJpaTest
@AutoConfigurationPackage
public class JiraTopicImporterTest {

    @Autowired
    private JiraTopicImporter jiraTopicImporter;

    @Test@Ignore
    public void testImport () throws InterruptedException {
        jiraTopicImporter.importTopicsOfUser(1L);

    }
}
