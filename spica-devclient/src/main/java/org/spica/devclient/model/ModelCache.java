package org.spica.devclient.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.model.EventInfo;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.model.TopicInfo;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@XmlRootElement
public class ModelCache {

  private File currentFile;

  private final static Logger LOGGER = LoggerFactory.getLogger(ModelCache.class);


  private List<ProjectInfo> projectInfos = new ArrayList<>();

  private List<TopicInfo> topicInfos = new ArrayList<>();



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
}
