package org.spica.server.project.service;

import org.spica.server.project.domain.Topic;

import java.util.List;

public interface TopicImporter {

    public List<Topic> importTopicsOfUser (final Long userID) throws InterruptedException;
}
