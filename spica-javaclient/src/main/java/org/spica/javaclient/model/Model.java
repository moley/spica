package org.spica.javaclient.model;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlRootElement;
import org.spica.javaclient.exceptions.NotFoundException;
import org.spica.javaclient.model.MessagecontainerInfo.MessagecontainerStateEnum;

/**
 * the spica model root element
 */
@XmlRootElement
public class Model {

  public final static String DEFAULTTASK_PRIVATE = "Private";
  public final static String DEFAULTTASK_WORK = "Work";

  private UserInfo me;

  private List<SkillInfo> userSkills = new ArrayList<>();

  private List<UserInfo> userInfos = new ArrayList<>();

  private List<ProjectInfo> projectInfos = new ArrayList<>();

  private List<WorkingSetInfo> workingsetInfos = new ArrayList<>();

  private List<TaskInfo> taskInfos = new ArrayList<>();

  private List<MessagecontainerInfo> messagecontainerInfos = new ArrayList<>();

  private List<LinkInfo> linkInfos = new ArrayList<>();

  private List<EventInfo> eventInfosReal = new ArrayList<>();

  private List<EventInfo> eventInfosPlanned = new ArrayList<>();

  private List<SkillInfo> allSkills = new ArrayList<>();

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
  public Collection<ProjectInfo> getOtherProjectInfos (final ProjectInfo selectedProjectInfo) {
    Collection<ProjectInfo> projectInfos = new ArrayList<>(getProjectInfos());
    projectInfos.remove(selectedProjectInfo);
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

					  boolean recipientsContainFrom = false;
					  for (String next: nextMessage.getRecieversTo()) {
					    if (next.equalsIgnoreCase(from.getId()))
					      recipientsContainFrom = true;
            }
					  if (nextMessage.getCreator() != null && (nextMessage.getCreator().equalsIgnoreCase(from.getId()) || recipientsContainFrom))
					    return nextContainer;
					}
				}
			}
		}
		
		return null;

	}

	public MessageInfo createNewMessage (MessageType type) {
	  MessageInfo messageInfo = new MessageInfo();
    messageInfo.setCreationtime(LocalDateTime.now());
    messageInfo.setCreator(getMe().getId());
    messageInfo.setType(type);
    messageInfo.setId(UUID.randomUUID().toString());
    return messageInfo;
  }

  public MessagecontainerInfo createNewMessageContainer () {
	  MessagecontainerInfo messagecontainerInfo = new MessagecontainerInfo();
	  messagecontainerInfo.setMessage(new ArrayList<>());
	  return messagecontainerInfo;
  }

  public UserInfo getUserNotNull (final String searchString) throws NotFoundException {
    UserInfo foundUser;
    try {
      foundUser = findUserBySearchString(searchString);
    } catch (NotFoundException e) {
      foundUser = new UserInfo();
      foundUser.setId(UUID.randomUUID().toString());
      if (searchString.startsWith("@"))
        throw new NotFoundException("User with username " + searchString.substring(1) + " not found");
      else if (searchString.contains("@")) {
        foundUser.setEmail(searchString);
        foundUser.setDisplayname(searchString);
      }
      else
        throw new NotFoundException("parameter " + searchString + " is not a username and mail adresse and user with this id not found");

      userInfos.add(foundUser);
    }
    return foundUser;
  }

  public List<UserInfo> getAllUsers (final MessagecontainerInfo messagecontainerInfo) throws NotFoundException {
    UserInfo me = getMe();
    Collection<UserInfo> otherUsers = new HashSet<>();
    for (MessageInfo nextMessage: messagecontainerInfo.getMessage()) {
      if (nextMessage.getRecieversTo() != null) {
        for (String next : nextMessage.getRecieversTo()) {
          UserInfo foundUser = getUserNotNull(next);
          otherUsers.add(foundUser);
        }
      }

      if (nextMessage.getCreator() != null) {
        UserInfo foundUser = getUserNotNull(nextMessage.getCreator());
        otherUsers.add(foundUser);
      }
    }

    return new ArrayList<>(otherUsers);
  }

  public List<UserInfo> getOtherUsers (final MessagecontainerInfo messagecontainerInfo) throws NotFoundException {
    UserInfo me = getMe();
    Collection<UserInfo> otherUsers = new HashSet<>();
    for (MessageInfo nextMessage: messagecontainerInfo.getMessage()) {
      if (nextMessage.getRecieversTo() != null) {
        for (String next : nextMessage.getRecieversTo()) {
          UserInfo foundUser = getUserNotNull(next);
          if (! foundUser.getId().equalsIgnoreCase(me.getId()))
            otherUsers.add(foundUser);
        }
      }

      if (nextMessage.getCreator() != null) {
        UserInfo foundUser = getUserNotNull(nextMessage.getCreator());
        if (! foundUser.getId().equalsIgnoreCase(me.getId()))
          otherUsers.add(foundUser);
      }
    }

    return new ArrayList<>(otherUsers);
  }

  public List<UserInfo> getCreators (final MessagecontainerInfo messagecontainerInfo) throws NotFoundException {
    UserInfo me = getMe();
    Collection<UserInfo> otherUsers = new HashSet<>();
    for (MessageInfo nextMessage: messagecontainerInfo.getMessage()) {
      if (nextMessage.getCreator() != null) {
        UserInfo foundUser = getUserNotNull(nextMessage.getCreator());
        if (! foundUser.getId().equalsIgnoreCase(me.getId()))
          otherUsers.add(foundUser);
      }
    }

    if (otherUsers.isEmpty())
      otherUsers.add(getMe());

    return new ArrayList<>(otherUsers);
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
      return new ArrayList<>();

    Predicate<TaskInfo> filter = taskInfoInfo -> {
      String nameNotNull = taskInfoInfo.getName() != null ? taskInfoInfo.getName() : "";
      String externalSystemKeyNotNull = taskInfoInfo.getExternalSystemKey() != null ?
          taskInfoInfo.getExternalSystemKey() :
          "";
      String idNotNull = taskInfoInfo.getId() != null ? taskInfoInfo.getId() : "";
      return nameNotNull.contains(query) || externalSystemKeyNotNull.equals(query) || idNotNull.equals(query);
    };
    return taskInfos.stream().filter( filter).collect(Collectors.toList());
  }

  /**
   * find projects by name or id
   * @param query  searchstring
   * @return list of found projects
   */
  public List<ProjectInfo> findProjectInfosByQuery (String query) {
    Predicate<ProjectInfo> filter = projectInfo -> {
      String nameNotNull = projectInfo.getName() != null ? projectInfo.getName(): "";
      String idNotNull = projectInfo.getId() != null ? projectInfo.getId(): "";
      return nameNotNull.contains(query) || idNotNull.equals(query);
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
    Predicate<WorkingSetInfo> filter = projectInfo -> {
      String nameNotNull = projectInfo.getName() != null ? projectInfo.getName() : "";
      String idNotNull = projectInfo.getId() != null ? projectInfo.getId() : "";
      return nameNotNull.contains(query) || idNotNull.equals(query);
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
    Predicate<LinkInfo> filter = linkInfo -> {
      String nameNotNull = linkInfo.getName() != null ? linkInfo.getName() : "";
      String idNotNull = linkInfo.getId() != null ? linkInfo.getId() : "";
      String urlNotNull = linkInfo.getUrl() != null ? linkInfo.getUrl() : "";
      return nameNotNull.contains(query) || idNotNull.equals(query) || urlNotNull.contains(query);
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
    if (eventInfo != null && eventInfo.getEventType().equals(EventType.TASK)) {
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
    List<EventInfo> oldEventInfos = new ArrayList<>();
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
   * finds only the users which have the source
   * @param source source
   * @return users with source
   */
  public List<UserInfo> findUsersBySource (final String source) {
    List<UserInfo> filteredUserInfos = new ArrayList<>();
    for (UserInfo next: getUserInfos()) {
      if (next.getSource() != null && next.getSource().equalsIgnoreCase(source))
        filteredUserInfos.add(next);
    }

    return filteredUserInfos;

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
   * get other tasks (excludes the currently selected one)
   *
   * @return other tasks
   */
  public Collection<TaskInfo> getOtherTaskInfos (TaskInfo selectedTaskInfo) {
    Collection<TaskInfo> taskInfos = new ArrayList<>(getTaskInfos());
    taskInfos.remove(selectedTaskInfo);
    return taskInfos;
  }


  /**
   * find user by ID
   * @param id user id
   * @return user or throws {@link IllegalStateException}
   */
  public UserInfo findUserById(String id) throws NotFoundException {

    if (id == null)
      throw new IllegalArgumentException("Parameter id must not be null");

    if (id.startsWith("@"))
      id = id.substring(1);

    for (UserInfo next: userInfos) {
      if (next.getId().equals(id))
        return next;
    }
    throw new NotFoundException("No user found for id '" + id + "'");
  }

  /**
   * find user:
   * - by username when param starts with @
   * - by mail when param contains @
   * - or by id
   *
   * If nothing found, an error is thrown
   *
   * @param param
   * @return user, not null
   * @throws NotFoundException if no user was found
   */
  public UserInfo findUserBySearchString (String param) throws NotFoundException {
    UserInfo userInfo = null;
    if (param != null && ! param.trim().isEmpty()) {
      if (param.startsWith("@"))
        userInfo = findUserByUsername(param.substring(1));

      if (userInfo == null && param.contains("@"))
        userInfo = findUserByMail(param);

      if (userInfo == null)
        userInfo = findUserById(param);
    }

    if (userInfo == null)
      throw new NotFoundException("No user found for searchstring '" + param + "'");

    return userInfo;
  }

  /**
   * find user by usernmae
   * @param username  username
   * @return user
   * @throws NotFoundException is thrown when no user for this username was found
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
    getMessagecontainerInfos().sort((o1, o2) -> {
      MessageInfo lastMessage1 = o1.getMessage().get(o1.getMessage().size() - 1);
      MessageInfo lastMessage2 = o2.getMessage().get(o2.getMessage().size() - 1);
      return lastMessage2.getCreationtime().compareTo(lastMessage1.getCreationtime());
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

  public List<String> getMailAdresses(String text) throws NotFoundException {
    List<String> mailadresses = new ArrayList<>();
    if (text != null && ! text.trim().isEmpty()) {
      String[] arrays = text.split(",");
      for (String next : arrays) {
        String trimmed = next.trim();
        if (trimmed.startsWith("@")) {
          UserInfo userInfo = findUserById(trimmed.substring(1));
          if (userInfo.getEmail() == null)
            throw new IllegalStateException("User " + userInfo.getUsername() + " does not have an mailadress");
          mailadresses.add(userInfo.getEmail());
        } else
          mailadresses.add(trimmed);
      }
    }
    return mailadresses;
  }

  public MessageInfo getLastMessageInMessageContainer(MessagecontainerInfo selectedItem) {
    return selectedItem.getMessage().get(selectedItem.getMessage().size() - 1);
  }
}
