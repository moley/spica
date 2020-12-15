package org.spica.server.project.service;

import org.spica.server.project.domain.Task;

import java.util.List;

public interface TaskImporter {

    List<Task> importTasksOfUser(final String userID, final String user, final String pwd) throws InterruptedException;
}
