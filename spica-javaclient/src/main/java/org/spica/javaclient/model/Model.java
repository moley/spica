package org.spica.javaclient.model;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlRootElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.DashboardItemType;

@XmlRootElement
public class Model {

  public final static String DEFAULTTASK_PRIVATE = "Private";
  public final static String DEFAULTTASK_WORK = "Work";

  private File currentFile;

  private final static Logger log = LoggerFactory.getLogger(Model.class);

  private UserInfo me;

  private List<DashboardItemInfo> dashboardItemInfos = new ArrayList<DashboardItemInfo>();

  private List<SkillInfo> userSkills = new ArrayList<>();

  private List<UserInfo> userInfos = new ArrayList<>();

  private List<ProjectInfo> projectInfos = new ArrayList<>();

  private List<WorkingSetInfo> workingsetInfos = new ArrayList<>();

  private List<TaskInfo> taskInfos = new ArrayList<>();

  private List<MessagecontainerInfo> messagecontainerInfos = new ArrayList<MessagecontainerInfo>();

  private List<LinkInfo> linkInfos = new ArrayList<LinkInfo>();

  private List<EventInfo> eventInfosReal = new ArrayList<>();

  private List<EventInfo> eventInfosPlanned = new ArrayList<>();

  private List<SkillInfo> allSkills = new ArrayList<SkillInfo>();

  private MessagecontainerInfo selectedMessageContainer;

  private TaskInfo selectedTaskInfo;

  private ProjectInfo selectedProjectInfo;


  public File getCurrentFile() {
    return currentFile;
  }

  public void setCurrentFile(File currentFile) {
    this.currentFile = currentFile;
  }

  public List<ProjectInfo> getProjectInfos() {
    return projectInfos;
  }

  public List<WorkingSetInfo> getWorkingsetInfos () {
    return workingsetInfos;
  }

  public void setWorkingsetInfos (final List<WorkingSetInfo> workingsetInfos) {
    this.workingsetInfos = workingsetInfos;
  }

  public Collection<ProjectInfo> getOtherProjectInfos () {
    Collection<ProjectInfo> projectInfos = new ArrayList<>();
    projectInfos.addAll(getProjectInfos());
    projectInfos.remove(getSelectedProjectInfo());
    return projectInfos;
  }

  public void setProjectInfos(List<ProjectInfo> projectInfos) {
    this.projectInfos = projectInfos;
  }

  public List<TaskInfo> findTaskInfosByQuery (String query) {
    if (query == null)
      return new ArrayList<TaskInfo>();

    Predicate<TaskInfo> filter = new Predicate<TaskInfo>() {
      @Override
      public boolean test(TaskInfo taskInfoInfo) {
        String nameNotNull = taskInfoInfo.getName() != null ? taskInfoInfo.getName(): "";
        String externalSystemKeyNotNull = taskInfoInfo.getExternalSystemKey() != null ? taskInfoInfo.getExternalSystemKey(): "" ;
        String idNotNull = taskInfoInfo.getId() != null ? taskInfoInfo.getId(): "";
        return nameNotNull.contains(query) || externalSystemKeyNotNull.equals(query) || idNotNull.equals(query);
      }
    };
    return taskInfos.stream().filter( filter).collect(Collectors.toList());
  }


  public List<ProjectInfo> findProjectInfosByQuery (String query) {
    Predicate<ProjectInfo> filter = new Predicate<ProjectInfo>() {
      @Override
      public boolean test(ProjectInfo projectInfo) {
        String nameNotNull = projectInfo.getName() != null ? projectInfo.getName(): "";
        String idNotNull = projectInfo.getId() != null ? projectInfo.getId(): "";
        return nameNotNull.contains(query) || idNotNull.equals(query);
      }
    };
    return projectInfos.stream().filter( filter).collect(Collectors.toList());

  }

  public ProjectInfo findProjectInfoById(String id) {
    if (id == null)
      return null;

    for (ProjectInfo next: projectInfos) {
      if (next.getId().equals(id))
        return next;
    }

    return null;
  }

  public List<WorkingSetInfo> findWorkingSetInfosByQuery (String query) {
    Predicate<WorkingSetInfo> filter = new Predicate<WorkingSetInfo>() {
      @Override
      public boolean test(WorkingSetInfo projectInfo) {
        String nameNotNull = projectInfo.getName() != null ? projectInfo.getName(): "";
        String idNotNull = projectInfo.getId() != null ? projectInfo.getId(): "";
        return nameNotNull.contains(query) || idNotNull.equals(query);
      }
    };
    return workingsetInfos.stream().filter( filter).collect(Collectors.toList());

  }

  public WorkingSetInfo findWorkingSetInfoById(String id) {
    if (id == null)
      return null;

    for (WorkingSetInfo next: workingsetInfos) {
      if (next.getId().equals(id))
        return next;
    }

    return null;
  }



  public List<LinkInfo> findLinkInfosByQuery (String query) {
    Predicate<LinkInfo> filter = new Predicate<LinkInfo>() {
      @Override
      public boolean test(LinkInfo linkInfo) {
        String nameNotNull = linkInfo.getName() != null ? linkInfo.getName(): "";
        String idNotNull = linkInfo.getId() != null ? linkInfo.getId(): "";
        String urlNotNull = linkInfo.getUrl() != null ? linkInfo.getUrl() : "";
        return nameNotNull.contains(query) || idNotNull.equals(query) || urlNotNull.contains(query);
      }
    };
    return linkInfos.stream().filter( filter).collect(Collectors.toList());

  }

  public TaskInfo findTaskInfoById (final String id) {
    return taskInfos.stream().filter(taskInfo -> taskInfo.getId().equals(id)).findAny().orElse(null);
  }



  public TaskInfo findTaskInfoByExternalSystemKey (final String id) {
    return taskInfos.stream().filter(taskInfo -> taskInfo.getExternalSystemKey().equals(id)).findAny().orElse(null);
  }

  public List<TaskInfo> getTaskInfos() {
    return taskInfos;
  }

  public void setTaskInfos(List<TaskInfo> taskInfos) {
    this.taskInfos = taskInfos;
  }

  public List<EventInfo> getEventInfosRealToday () {
    LocalDate today = LocalDate.now();
    return getEventInfosReal().stream().filter(info->info.getStart().toLocalDate().equals(today)).collect(Collectors.toList());
  }

  public List<EventInfo> getEventInfosReal() {
    return eventInfosReal;
  }




  public EventInfo findEventInfoRealById (final String id) {
    return eventInfosReal.stream().filter(eventInfo -> eventInfo.getId().equals(id)).findAny().orElse(null);
  }

  public LinkInfo findLinkInfoById (final String id) {
    return linkInfos.stream().filter(linkInfo -> linkInfo.getId().equals(id)).findAny().orElse(null);
  }


  public void setEventInfosReal(List<EventInfo> eventInfosReal) {
    this.eventInfosReal = eventInfosReal;
  }

  public List<EventInfo> getEventInfosPlanned() {
    return eventInfosPlanned;
  }

  public void setEventInfosPlanned(List<EventInfo> eventInfosPlanned) {
    this.eventInfosPlanned = eventInfosPlanned;
  }

  public EventInfo findEventBefore (LocalDateTime currentDateTime) {
    EventInfo last = null;
    for (EventInfo next: getEventInfosRealToday()) {
      if (next.getStart().isBefore(currentDateTime)) {
        last = next;
      }
    }

    return last;
  }

  public EventInfo findEventAfter (LocalDateTime currentDateTime) {
    for (EventInfo next: getEventInfosRealToday()) {
      if (next.getStart().isAfter(currentDateTime)) {
        return next;
      }
    }
    return null;
  }

  public TaskInfo getCurrentTask () {
    EventInfo eventInfo = findLastOpenEventFromToday();
    if (eventInfo != null && eventInfo.getEventType().equals(EventType.TOPIC)) {
      return findTaskInfoById(eventInfo.getReferenceId());
    }
    return null;
  }

  public List<EventInfo> findOldOpenEvents () {
    LocalDate today = LocalDate.now();
    List<EventInfo> oldEventInfos = new ArrayList<EventInfo>();
    for (EventInfo next: getEventInfosReal()) {
      if (next.getStop() == null && next.getStart().toLocalDate().isBefore(today)) {
        oldEventInfos.add(next);
      }
    }

    return oldEventInfos;
  }

  public EventInfo findLastOpenEventFromToday() {
    EventInfo last = null;
    for (EventInfo next: getEventInfosRealToday()) {
      if (next.getStop() == null) {
        if (last == null)
          last = next;
        else
          throw new IllegalStateException("Implementation error, more than one open event found");
      }
    }

    return last;

  }


  public List<UserInfo> getUserInfos() {
    return userInfos;
  }

  public void setUserInfos(List<UserInfo> userInfos) {
    this.userInfos = userInfos;
  }

  public List<MessagecontainerInfo> getMessagecontainerInfos() {
    return messagecontainerInfos;
  }

  public void setMessagecontainerInfos(List<MessagecontainerInfo> messagecontainerInfos) {
    this.messagecontainerInfos = messagecontainerInfos;
  }

  public List<LinkInfo> getLinkInfos() {
    return linkInfos;
  }

  public void setLinkInfos(List<LinkInfo> linkInfos) {
    this.linkInfos = linkInfos;
  }

  public List<SkillInfo> getAllSkills() {
    if (allSkills == null)
      allSkills = new ArrayList<>();
    return allSkills;
  }

  public SkillInfo findSkillByName (final String name) {
    for (SkillInfo next: allSkills) {
      if (next.getName().trim().equalsIgnoreCase(name.trim())) {
        return next;
      }
    }
    return null;
  }

  public void setAllSkills(List<SkillInfo> allSkills) {
    this.allSkills = allSkills;
  }

  public UserInfo getMe() {
    return me;
  }

  public boolean isMe (final UserInfo userInfo) {
    if (me == null)
      throw new IllegalStateException("Me cannot be null");

    if (userInfo == null)
      throw new IllegalStateException("Argument 'userInfo' must not be null");

    return userInfo.getId().equals(me.getId());
  }

  public void setMe(UserInfo me) {
    this.me = me;
  }

  public List<SkillInfo> getUserSkills() {
    return userSkills;
  }

  public void setUserSkills(List<SkillInfo> userSkills) {
    this.userSkills = userSkills;
  }

  public List<DashboardItemInfo> getDashboardItemInfos() {
    return dashboardItemInfos;
  }

  public DashboardItemInfo findDashboardItemInfo (final DashboardItemType dashboardItemType, final String referenceId) {

    for (DashboardItemInfo next: dashboardItemInfos) {
      if (next.getItemReference().equals(referenceId) && next.getItemType().equals(dashboardItemType.name())) {
        return next;
      }
    }
    return null;
  }

  public void setDashboardItemInfos(List<DashboardItemInfo> dashboardItemInfos) {
    this.dashboardItemInfos = dashboardItemInfos;
  }

  public MessagecontainerInfo getSelectedMessageContainer() {
    return selectedMessageContainer;
  }

  public void setSelectedMessageContainer(MessagecontainerInfo selectedMessageContainer) {
    this.selectedMessageContainer = selectedMessageContainer;
  }

  public TaskInfo getSelectedTaskInfo() {
    return selectedTaskInfo;
  }

  public Collection<TaskInfo> getOtherTaskInfos () {
    Collection<TaskInfo> taskInfos = new ArrayList<>();
    taskInfos.addAll(getTaskInfos());
    taskInfos.remove(getSelectedTaskInfo());
    return taskInfos;
  }

  public void setSelectedTaskInfo(TaskInfo selectedTaskInfo) {
    this.selectedTaskInfo = selectedTaskInfo;
  }

  public UserInfo findUserById(String id) {
    if (id == null)
      throw new IllegalArgumentException("Parameter id must not be null");

    for (UserInfo next: userInfos) {
      if (next.getId().equals(id))
        return next;
    }
    throw new IllegalStateException("No user found for username " + id);
  }

  public UserInfo findUserByUsername(String username) {
    if (username == null)
      throw new IllegalArgumentException("Parameter username must not be null");

    for (UserInfo next: userInfos) {
      if (next.getUsername() != null && next.getUsername().equals(username))
        return next;
    }
    throw new IllegalStateException("No user found for username " + username);
  }

  public UserInfo findUserByMail(String mail) {
    if (mail == null)
      throw new IllegalArgumentException("Parameter mail must not be null");

    for (UserInfo next: userInfos) {
      if (next.getEmail() != null && next.getEmail().equalsIgnoreCase(mail))
        return next;
    }
    throw new IllegalStateException("No user found for mail " + mail);
  }

  public ProjectInfo getSelectedProjectInfo() {
    return selectedProjectInfo;
  }

  public void setSelectedProjectInfo(ProjectInfo selectedProjectInfo) {
    this.selectedProjectInfo = selectedProjectInfo;
  }

  public List<ProjectInfo> getProjectsOfTask(TaskInfo nextTaskInfo) {
    List<ProjectInfo> projects = new ArrayList<>();
    if (nextTaskInfo.getProjectId() != null) {
      ProjectInfo project = findProjectInfoById(nextTaskInfo.getProjectId());
      projects.add(project);
      while (project.getParentId() != null) {
        ProjectInfo parent = findProjectInfoById(project.getParentId());
        projects.add(parent);
        project = parent;
      }
    }

    return projects;
  }
}
