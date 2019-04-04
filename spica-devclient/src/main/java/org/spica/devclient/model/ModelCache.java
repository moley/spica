package org.spica.devclient.model;

import com.google.gson.annotations.SerializedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.model.TopicInfo;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement
public class ModelCache {

  private File currentFile;

  private final static Logger LOGGER = LoggerFactory.getLogger(ModelCache.class);


  private List<ProjectInfo> projectInfos = new ArrayList<>();

  private List<TopicInfo> topicInfos = new ArrayList<>();

  private List<EventInfo> eventInfosReal = new ArrayList<>();

  private List<EventInfo> eventInfosPlanned = new ArrayList<>();

  @SerializedName("start")
  private LocalDateTime something = null;



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



  public void setEventInfosReal(List<EventInfo> eventInfosReal) {
    this.eventInfosReal = eventInfosReal;
  }

  public List<EventInfo> getEventInfosPlanned() {
    return eventInfosPlanned;
  }

  public void setEventInfosPlanned(List<EventInfo> eventInfosPlanned) {
    this.eventInfosPlanned = eventInfosPlanned;
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

  public LocalDateTime getSomething() {
    return something;
  }

  public void setSomething(LocalDateTime something) {
    this.something = something;
  }
}
