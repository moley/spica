package org.spica.javaclient.actions;

import org.spica.javaclient.api.ProjectApi;
import org.spica.javaclient.api.TaskApi;
import org.spica.javaclient.api.UserApi;

public class Api {

  private UserApi userApi;

  private TaskApi taskApi;

  private ProjectApi projectApi;

  /**
   * getter UserAPI
   * @return user api
   */
  public UserApi getUserApi() {
    if (userApi == null)
      userApi = new UserApi();
    return userApi;
  }

  /**
   * getter basepath of current server
   * @return current server
   */
  public String getCurrentServer() {
    return getUserApi().getApiClient().getBasePath();
  }

  /**
   * getter TaskAPI
   * @return task api
   */
  public TaskApi getTaskApi() {
    if (taskApi == null) {
      taskApi = new TaskApi();
    }
    return taskApi;
  }

  /**
   * getter ProjectAPI
   * @return project api
   */
  public ProjectApi getProjectApi() {

    if (projectApi == null) {
      projectApi = new ProjectApi();
    }
    return projectApi;
  }
}
