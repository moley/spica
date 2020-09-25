package org.spica.javaclient.model;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.DashboardItemType;
import org.spica.javaclient.exceptions.NotFoundException;
import org.spica.javaclient.model.MessagecontainerInfo.MessagecontainerStateEnum;

/**
 * the spica model root element
 */
@XmlRootElement
public class Model {

  public final static String DEFAULTTASK_PRIVATE = "Private";
  public final static String DEFAULTTASK_WORK = "Work";

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

  /**
   * getter
   * @return list of all projects
   */
  public List<ProjectInfo> getProjectInfos() {
    return projectInfos;
  }

  /**
   * getter
   * @return list of all workingsets
   */
  public List<WorkingSetInfo> getWorkingsetInfos () {
    return workingsetInfos;
  }

  /**
   * setter
   * <b>ATTENTION! overrides the current data</b>
   * @param workingsetInfos list of workingsets
   */
  public void setWorkingsetInfos (final List<WorkingSetInfo> workingsetInfos) {
    this.workingsetInfos = workingsetInfos;
  }

  /**
   * get all project infos excluding the one that is selected
   * @return other projects
   */
  public Collection<ProjectInfo> getOtherProjectInfos () {
    Collection<ProjectInfo> projectInfos = new ArrayList<>();
    projectInfos.addAll(getProjectInfos());
    projectInfos.remove(getSelectedProjectInfo());
    return projectInfos;
  }

  /**
   * setter
   * <b>ATTENTION! overrides the current data</b>
   * @param projectInfos  list of projects
   */
  public void setProjectInfos(List<ProjectInfo> projectInfos) {
    this.projectInfos = projectInfos;
  }
  
  /**
   * finds an open message container which contains a message with the given message type and 
   * the mail address matching from
   * 
   * @param messageType   message type found
   * @param from          from found
   * @return found message container or <code>null</code> if none is found
   */
	public MessagecontainerInfo findOpenMessageContainerByUser(final MessageType messageType, final UserInfo from) {
		for (MessagecontainerInfo nextContainer : messagecontainerInfos) {
			if (nextContainer.getMessagecontainerState() == null || !nextContainer.getMessagecontainerState().equals(MessagecontainerStateEnum.FINISHED)) {
				for (MessageInfo nextMessage : nextContainer.getMessage()) {
					if (nextMessage.getType() != null && nextMessage.getType().equals(messageType)) {
					  if (nextMessage.getCreatorId() != null && (nextMessage.getCreatorId().equalsIgnoreCase(from.getId()) ||
                nextMessage.getRecipientId().equalsIgnoreCase(from.getId())))
					    return nextContainer;
					}
				}
			}
		}
		
		return null;

	}

  public List<UserInfo> getOtherUsers (final MessagecontainerInfo messagecontainerInfo) {
    UserInfo me = getMe();
    Collection<UserInfo> userInfos = new HashSet<>();
    for (MessageInfo nextMessage: messagecontainerInfo.getMessage()) {
      if (nextMessage.getRecipientId() != null && ! nextMessage.getRecipientId().equals(me.getId()))
        userInfos.add(findUserById(nextMessage.getRecipientId()));

      if (nextMessage.getCreatorId() != null && ! nextMessage.getCreatorId().equals(me.getId()))
        userInfos.add(findUserById(nextMessage.getCreatorId()));
    }

    return new ArrayList<>(userInfos);
  }

	public UserInfo getUsersOrMe (final MessagecontainerInfo messagecontainerInfo) {
	  UserInfo me = getMe();
	  Collection<UserInfo> userInfos = getOtherUsers(messagecontainerInfo);
	  if (userInfos.size() > 1)
	    throw new IllegalStateException("More than one user found, which is not me in messagecontainer " + messagecontainerInfo.getTopic());
	  else if (userInfos.isEmpty())
	    return getMe();

	  return userInfos.iterator().next();

  }

  /**
   * find tasks by name, external system key or id
   *
   * @param query searchstring
   *
   * @return list of found tasks
   */
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

  /**
   * find projects by name or id
   * @param query  searchstring
   * @return list of found projects
   */
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

  /**
   * find project by id
   * @param id  project id
   * @return project or <code>null</code>
   */
  public ProjectInfo findProjectInfoById(String id) {
    if (id == null)
      return null;

    for (ProjectInfo next: projectInfos) {
      if (next.getId().equals(id))
        return next;
    }

    return null;
  }

  /**
   * find workingsets by name or id
   * @param query searchstring
   * @return list of found workings sets
   */
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

  /**
   * find a workingset sourcepart due to it's branch and clone url
   *
   * @param workingSetInfo    searching in this workingset
   * @param url               url to be searched
   * @param branch            branch to be searched
   * @return a found workingset sourcepart or <code>null</code>
   */
  public WorkingSetSourcePartInfo findWorkingSetSourcePart (final WorkingSetInfo workingSetInfo, final String url, final String branch) {
    if (workingSetInfo.getSourceparts() == null)
      return null;

    for (WorkingSetSourcePartInfo nextSourcePart: workingSetInfo.getSourceparts()) {
      if (nextSourcePart.getUrl().equalsIgnoreCase(url) && nextSourcePart.getBranch().equalsIgnoreCase(branch))
        return nextSourcePart;
    }

    return null;

  }

  /**
   * finds the workingset which is associated with the folder
   * @param folder    folder, which is associated to a workingset
   * @return workingset or <code>null</code> if no matching working set was found
   */
  public WorkingSetInfo findWorkingSetInfoByFolder (final File folder) {

    for (WorkingSetInfo nextWorkingsSet: workingsetInfos) {
      if (nextWorkingsSet.getLocalFolder().equalsIgnoreCase(folder.getAbsolutePath()))
        return nextWorkingsSet;
    }

    return null;

  }

  /**
   * find workingset by id
   * @param id  working set id
   * @return the workingset or <code>null</code>
   */
  public WorkingSetInfo findWorkingSetInfoById(String id) {
    if (id == null)
      return null;

    for (WorkingSetInfo next: workingsetInfos) {
      if (next.getId().equals(id))
        return next;
    }

    return null;
  }

  /**
   * find links by name, url or id
   * @param query searchstring
   * @return list of found links
   */
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

  /**
   * find task by ID
   * @param id  ID to find
   * @return task or <code>null</code>
   */
  public TaskInfo findTaskInfoById (final String id) {
    return taskInfos.stream().filter(taskInfo -> taskInfo.getId().equals(id)).findAny().orElse(null);
  }

  /**
   * find task by external system key
   * @param key  key
   * @return task info or <code>null</code>
   */
  public TaskInfo findTaskInfoByExternalSystemKey (final String key) {
    return taskInfos.stream().filter(taskInfo -> taskInfo.getExternalSystemKey().equals(key)).findAny().orElse(null);
  }

  /**
   * getter
   * @return list of all tasks
   */
  public List<TaskInfo> getTaskInfos() {
    return taskInfos;
  }

  /**
   * setter
   * <b>ATTENTION! Overrids current data</b>
   * @param taskInfos list of tasks
   */
  public void setTaskInfos(List<TaskInfo> taskInfos) {
    this.taskInfos = taskInfos;
  }

  /**
   * get real events from today
   * @return list of events
   */
  public List<EventInfo> getEventInfosRealToday () {
    LocalDate today = LocalDate.now();
    return getEventInfosReal().stream().filter(info->info.getStart().toLocalDate().equals(today)).collect(Collectors.toList());
  }

  /**
   * get list of all real events
   * @return list of events
   */
  public List<EventInfo> getEventInfosReal() {
    return eventInfosReal;
  }

  /**
   * find the real event by id
   * @param id id of event
   * @return real event or <code>null</code>
   */
  public EventInfo findEventInfoRealById (final String id) {
    return eventInfosReal.stream().filter(eventInfo -> eventInfo.getId().equals(id)).findAny().orElse(null);
  }

  /**
   * find link by id
   *
   * @param id link id
   * @return link or <code>null</code>
   */
  public LinkInfo findLinkInfoById (final String id) {
    return linkInfos.stream().filter(linkInfo -> linkInfo.getId().equals(id)).findAny().orElse(null);
  }

  /**
   * setter event infos real
   * <b>ATTENTION! Overrids current data</b>
   * @param eventInfosReal list of event infos
   */
  public void setEventInfosReal(List<EventInfo> eventInfosReal) {
    this.eventInfosReal = eventInfosReal;
  }

  /**
   * getter events planned
   * @return list of planned events
   */
  public List<EventInfo> getEventInfosPlanned() {
    return eventInfosPlanned;
  }

  /**
   * setter event infos planned
   * <b>ATTENTION! Overrids current data</b>
   * @param eventInfosPlanned list of event infos
   */
  public void setEventInfosPlanned(List<EventInfo> eventInfosPlanned) {
    this.eventInfosPlanned = eventInfosPlanned;
  }

  /**
   * find the last started event relative to the timestamp
   *
   * @param currentDateTime timestamp
   *
   * @return event
   */
  public EventInfo findEventBefore (LocalDateTime currentDateTime) {
    EventInfo last = null;
    for (EventInfo next: getEventInfosRealToday()) {
      if (next.getStart().isBefore(currentDateTime)) {
        last = next;
      }
    }

    return last;
  }

  /**
   * find the first event started after the timestamp
   *
   * @param currentDateTime timestamp
   *
   * @return event
   */
  public EventInfo findEventAfter (LocalDateTime currentDateTime) {
    for (EventInfo next: getEventInfosRealToday()) {
      if (next.getStart().isAfter(currentDateTime)) {
        return next;
      }
    }
    return null;
  }

  /**
   * find the current task
   * @return current task
   */
  public TaskInfo getCurrentTask () {
    EventInfo eventInfo = findLastOpenEventFromToday();
    if (eventInfo != null && eventInfo.getEventType().equals(EventType.TOPIC)) {
      return findTaskInfoById(eventInfo.getReferenceId());
    }
    return null;
  }

  /**
   * find old open events, which means
   * events from before today, which are not closed
   *
   * @return list of old open events
   */
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

  /**
   * find the last open event from today
   * @return event
   */
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

  /**
   * get list of all users
   * @return users
   */
  public List<UserInfo> getUserInfos() {
    return userInfos;
  }

  /**
   * setter users
   * <b>ATTENTION! Overrids current data</b>
   * @param userInfos list of users
   */
  public void setUserInfos(List<UserInfo> userInfos) {
    this.userInfos = userInfos;
  }

  /**
   * get all message containers
   * @return message container
   */
  public List<MessagecontainerInfo> getMessagecontainerInfos() {
    return messagecontainerInfos;
  }

  /**
   * setter messagecontainers
   *
   * <b>ATTENTION! Overrids current data</b>
   * @param messagecontainerInfos  list of messagecontainers
   */
  public void setMessagecontainerInfos(List<MessagecontainerInfo> messagecontainerInfos) {
    this.messagecontainerInfos = messagecontainerInfos;
  }

  /**
   * get all links
   * @return list of links
   */
  public List<LinkInfo> getLinkInfos() {
    return linkInfos;
  }

  /**
   * setter links
   *
   * <b>ATTENTION! Overrids current data</b>
   * @param linkInfos links
   */
  public void setLinkInfos(List<LinkInfo> linkInfos) {
    this.linkInfos = linkInfos;
  }

  /**
   * getter all skills
   * @return list of all skills
   */
  public List<SkillInfo> getAllSkills() {
    if (allSkills == null)
      allSkills = new ArrayList<>();
    return allSkills;
  }

  /**
   * find user skill by name
   * @param name  name to be found
   * @return skill
   */
  public SkillInfo findSkillByName (final String name) {
    for (SkillInfo next: allSkills) {
      if (next.getName().trim().equalsIgnoreCase(name.trim())) {
        return next;
      }
    }
    return null;
  }

  /**
   * setter skills
   *
   * <b>ATTENTION! Overrids current data</b>
   * @param allSkills skills
   */
  public void setAllSkills(List<SkillInfo> allSkills) {
    this.allSkills = allSkills;
  }

  /**
   * getter
   * @return logged in user
   */
  public UserInfo getMe() {
    return me;
  }

  /**
   * checks if user is the currently logged in user
   * @param userInfo  user
   * @return true: is the logged in user, false: is not the logged in user
   */
  public boolean isMe (final UserInfo userInfo) {
    if (me == null)
      throw new IllegalStateException("Me cannot be null");

    if (userInfo == null)
      throw new IllegalStateException("Argument 'userInfo' must not be null");

    return userInfo.getId().equals(me.getId());
  }

  /**
   * set the logged in user
   * @param me logged in user
   */
  public void setMe(UserInfo me) {
    this.me = me;
  }

  /**
   * getter
   * @return all user skills of the currently logged in user
   */
  public List<SkillInfo> getUserSkills() {
    return userSkills;
  }

  /**
   * setter userskill
   *
   * <b>ATTENTION! Overrids current data</b>
   * @param userSkills skills
   */
  public void setUserSkills(List<SkillInfo> userSkills) {
    this.userSkills = userSkills;
  }

  /**
   * getter
   * @return all dashboard items
   */
  public List<DashboardItemInfo> getDashboardItemInfos() {
    return dashboardItemInfos;
  }

  /**
   * find dashboard item by type and reference
   *
   * @param dashboardItemType   type
   * @param referenceId  reference id
   * @return dashboarditem
   */
  public DashboardItemInfo findDashboardItemInfo (final DashboardItemType dashboardItemType, final String referenceId) {

    for (DashboardItemInfo next: dashboardItemInfos) {
      if (next.getItemReference().equals(referenceId) && next.getItemType().equals(dashboardItemType.name())) {
        return next;
      }
    }
    return null;
  }

  /**
   * setter dasboarditems
   *
   * <b>ATTENTION! Overrids current data</b>
   * @param dashboardItemInfos dashboardItems
   */
  public void setDashboardItemInfos(List<DashboardItemInfo> dashboardItemInfos) {
    this.dashboardItemInfos = dashboardItemInfos;
  }

  /**
   * get the selected message container
   * @return message container
   */
  public MessagecontainerInfo getSelectedMessageContainer() {
    return selectedMessageContainer;
  }

  /**
   * select the message container
   * @param selectedMessageContainer message container
   * @return true: read time was added, caller should save the model
   */
  public boolean setSelectedMessageContainer(MessagecontainerInfo selectedMessageContainer) {
    boolean readtimeSet = false;
    for (MessageInfo nextMessage: selectedMessageContainer.getMessage()) {
      if (nextMessage.getReadtime() == null) {
        nextMessage.setReadtime(LocalDateTime.now());
        readtimeSet = true;
      }
    }
    this.selectedMessageContainer = selectedMessageContainer;
    return readtimeSet;
  }

  /**
   * get the selected task
   * @return selected task
   */
  public TaskInfo getSelectedTaskInfo() {
    return selectedTaskInfo;
  }

  /**
   * get other tasks (excludes the currently selected one)
   *
   * @return other tasks
   */
  public Collection<TaskInfo> getOtherTaskInfos () {
    Collection<TaskInfo> taskInfos = new ArrayList<>();
    taskInfos.addAll(getTaskInfos());
    taskInfos.remove(getSelectedTaskInfo());
    return taskInfos;
  }

  /**
   * selected a task
   * @param selectedTaskInfo selected task
   */
  public void setSelectedTaskInfo(TaskInfo selectedTaskInfo) {
    this.selectedTaskInfo = selectedTaskInfo;
  }

  /**
   * find user by ID
   * @param id user id
   * @return user or throws {@link IllegalStateException}
   */
  public UserInfo findUserById(String id) {
    if (id == null)
      throw new IllegalArgumentException("Parameter id must not be null");

    for (UserInfo next: userInfos) {
      if (next.getId().equals(id))
        return next;
    }
    throw new IllegalStateException("No user found for username " + id);
  }

  /**
   * find user by usernmae
   * @param username  username
   * @return user or {@link NotFoundException}
   */
  public UserInfo findUserByUsername(String username) throws NotFoundException {
    if (username == null)
      throw new IllegalArgumentException("Parameter username must not be null");

    for (UserInfo next: userInfos) {
      if (next.getUsername() != null && next.getUsername().equalsIgnoreCase(username))
        return next;
    }
    throw new NotFoundException("No user found for username " + username + " in list of " + userInfos.size() + " users");
  }

  /**
   * find user by mail
   * @param mail  mail
   * @return user or <code>null</code>
   */

  public UserInfo findUserByMail(String mail) {
    if (mail == null)
      throw new IllegalArgumentException("Parameter mail must not be null");

    for (UserInfo next: userInfos) {
      if (next.getEmail() != null && next.getEmail().equalsIgnoreCase(mail))
        return next;
    }
    return null;
  }

  /**
   * get the selected project
   * @return selected project
   */
  public ProjectInfo getSelectedProjectInfo() {
    return selectedProjectInfo;
  }

  /**
   * select a project
   * @param selectedProjectInfo project to select
   */
  public void setSelectedProjectInfo(ProjectInfo selectedProjectInfo) {
    this.selectedProjectInfo = selectedProjectInfo;
  }

  /**
   * get projects of task
   * This means the assigned project and all its parents
   * @param nextTaskInfo task
   * @return list of projects
   */
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

  public void sortMessages () {
    Collections
        .sort(getMessagecontainerInfos(), new Comparator<MessagecontainerInfo>() {
          @Override public int compare(MessagecontainerInfo o1, MessagecontainerInfo o2) {
            MessageInfo lastMessage1 = o1.getMessage().get(o1.getMessage().size() - 1);
            MessageInfo lastMessage2 = o2.getMessage().get(o2.getMessage().size() - 1);
            return lastMessage2.getCreationtime().compareTo(lastMessage1.getCreationtime());
          }
        });

  }

  public List<MessageInfo> findUnreadMessages() {
    List<MessageInfo> unreadMessages = new ArrayList<>();
    for (MessagecontainerInfo nextContainer: getMessagecontainerInfos()) {
      for (MessageInfo nextInfo: nextContainer.getMessage()) {
        if (nextInfo.getReadtime() == null)
          unreadMessages.add(nextInfo);
      }
    }

    return unreadMessages;
  }
}
