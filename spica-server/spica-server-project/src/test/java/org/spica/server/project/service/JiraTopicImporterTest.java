package org.spica.server.project.service;

import org.junit.Test;

public class JiraTopicImporterTest {

    @Test
    public void testImport () throws InterruptedException {
        JiraTopicImporter jiraTopicImporter = new JiraTopicImporter();

        jiraTopicImporter.importTopicsOfUser(1L);

    }
}
