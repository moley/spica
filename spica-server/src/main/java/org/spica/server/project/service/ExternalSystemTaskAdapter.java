package org.spica.server.project.service;

import org.spica.server.commons.ExternalSystem;
import org.spica.server.project.model.TaskInfo;

import java.util.Collection;

public interface ExternalSystemTaskAdapter {

    Collection<TaskInfo> getTasksOfUser (ExternalSystem externalSystem);

    String getStatus(ExternalSystem externalSystem, String s);
}
