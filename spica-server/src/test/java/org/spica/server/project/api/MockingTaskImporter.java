package org.spica.server.project.api;

import org.spica.server.project.domain.Task;
import org.spica.server.project.service.TaskImporter;

import java.util.ArrayList;
import java.util.List;

public class MockingTaskImporter implements TaskImporter {

    private static List<Task> taskCollection = new ArrayList<Task>();
    @Override
    public List<Task> importTasksOfUser(String userID, String user, String pwd) throws InterruptedException {
        if (taskCollection.isEmpty()) {
            Task task = Task.builder().name("topic").externalSystemKey("external").description("description").id("id").build();
            taskCollection.add(task);

        }
        return taskCollection;
    }
}
