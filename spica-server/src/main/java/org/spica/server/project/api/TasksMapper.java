package org.spica.server.project.api;

import lombok.extern.slf4j.Slf4j;
import org.spica.server.project.domain.Task;
import org.spica.server.project.model.TaskContainerInfo;
import org.spica.server.project.model.TaskInfo;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TasksMapper {

  TaskInfo toApi (Task task) {
    TaskInfo topicInfo = new TaskInfo();
    topicInfo.setId(task.getId());
    topicInfo.setName(task.getName());
    topicInfo.setDescription(task.getDescription());
    topicInfo.setExternalSystemID(task.getExternalSystemID());
    topicInfo.setExternalSystemKey(task.getExternalSystemKey());

    if (task.getParentTask() != null)
      topicInfo.setParentId(toApi(task.getParentTask()).getId());
    //topicInfo.setState();
    //topicInfo.setProject();

    return topicInfo;
  }

  TaskContainerInfo toApi (List<Task> tasks) {
    TaskContainerInfo topicContainerInfo = new TaskContainerInfo();
    topicContainerInfo.setTasks(new ArrayList<TaskInfo>());
    for (Task next: tasks) {
      topicContainerInfo.addTasksItem(toApi(next));
    }


    return topicContainerInfo;
  }
}
