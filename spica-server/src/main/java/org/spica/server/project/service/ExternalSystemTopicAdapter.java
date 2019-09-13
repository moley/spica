package org.spica.server.project.service;

import org.spica.server.commons.ExternalSystem;
import org.spica.server.project.model.TopicInfo;

import java.util.Collection;

public interface ExternalSystemTopicAdapter {

    Collection<TopicInfo> getTopicsOfUser (ExternalSystem externalSystem);

    String getStatus(ExternalSystem externalSystem, String s);
}
