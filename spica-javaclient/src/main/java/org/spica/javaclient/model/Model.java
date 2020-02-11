package org.spica.javaclient.model;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlRootElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@XmlRootElement
public class Model {

  private File currentFile;

  private final static Logger LOGGER = LoggerFactory.getLogger(Model.class);


  private List<UserInfo> userInfos = new ArrayList<>();

  private List<ProjectInfo> projectInfos = new ArrayList<>();

  private List<TopicInfo> topicInfos = new ArrayList<>();

  private List<MessagecontainerInfo> messagecontainerInfos = new ArrayList<MessagecontainerInfo>();

  private List<LinkInfo> linkInfos = new ArrayList<LinkInfo>();

  private List<EventInfo> eventInfosReal = new ArrayList<>();

  private List<EventInfo> eventInfosPlanned = new ArrayList<>();

  private List<SkillInfo> allSkills = new ArrayList<SkillInfo>();


  public File getCurrentFile() {
    return currentFile;
  }

  public void setCurrentFile(File currentFile) {
    this.currentFile = currentFile;
  }

  public List<ProjectInfo> getProjectInfos() {
    return projectInfos;
  }

  public void setProjectInfos(List<ProjectInfo> projectInfos) {
    this.projectInfos = projectInfos;
  }

  public List<TopicInfo> findTopicInfosByQuery (String query) {
    if (query == null)
      return new ArrayList<TopicInfo>();

    Predicate<TopicInfo> filter = new Predicate<TopicInfo>() {
      @Override
      public boolean test(TopicInfo topicInfo) {
        String nameNotNull = topicInfo.getName() != null ? topicInfo.getName(): "";
        String externalSystemKeyNotNull = topicInfo.getExternalSystemKey() != null ? topicInfo.getExternalSystemKey(): "" ;
        String idNotNull = topicInfo.getId() != null ? topicInfo.getId(): "";
        return nameNotNull.contains(query) || externalSystemKeyNotNull.equals(query) || idNotNull.equals(query);
      }
    };
    return topicInfos.stream().filter( filter).collect(Collectors.toList());
  }

  public List<ProjectInfo> findProjectInfosByQuery (String query) {
    Predicate<ProjectInfo> filter = new Predicate<ProjectInfo>() {
      @Override
      public boolean test(ProjectInfo topicInfo) {
        String nameNotNull = topicInfo.getName() != null ? topicInfo.getName(): "";
        String idNotNull = topicInfo.getId() != null ? topicInfo.getId(): "";
        return nameNotNull.contains(query) || idNotNull.equals(query);
      }
    };
    return projectInfos.stream().filter( filter).collect(Collectors.toList());

  }

  public List<LinkInfo> findLinkInfosByQuery (String query) {
    Predicate<LinkInfo> filter = new Predicate<LinkInfo>() {
      @Override
      public boolean test(LinkInfo topicInfo) {
        String nameNotNull = topicInfo.getName() != null ? topicInfo.getName(): "";
        String idNotNull = topicInfo.getId() != null ? topicInfo.getId(): "";
        String urlNotNull = topicInfo.getUrl() != null ? topicInfo.getUrl() : "";
        return nameNotNull.contains(query) || idNotNull.equals(query) || urlNotNull.contains(query);
      }
    };
    return linkInfos.stream().filter( filter).collect(Collectors.toList());

  }

  public TopicInfo findTopicInfoById (final String id) {
    return topicInfos.stream().filter(topicInfo -> topicInfo.getId().equals(id)).findAny().orElse(null);
  }



  public TopicInfo findTopicInfoByExternalSystemKey (final String id) {
    return topicInfos.stream().filter(topicInfo -> topicInfo.getExternalSystemKey().equals(id)).findAny().orElse(null);
  }

  public List<TopicInfo> getTopicInfos() {
    return topicInfos;
  }

  public void setTopicInfos(List<TopicInfo> topicInfos) {
    this.topicInfos = topicInfos;
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

  public TopicInfo getCurrentTopic () {
    EventInfo eventInfo = findLastOpenEventFromToday();
    if (eventInfo != null && eventInfo.getEventType().equals(EventType.TOPIC)) {
      return findTopicInfoById(eventInfo.getReferenceId());
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
}
