package org.spica.fx.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;
import org.spica.javaclient.model.MessageInfo;
import org.spica.javaclient.model.MessagecontainerInfo;

@Data
public class MessageDetailControllerModel {

  private StringProperty idProperty = new SimpleStringProperty();
  private StringProperty topicProperty = new SimpleStringProperty();
  private StringProperty typeProperty = new SimpleStringProperty();


  private StringProperty mailToProperty = new SimpleStringProperty();
  private StringProperty mailCCProperty = new SimpleStringProperty();
  private StringProperty mailBCCProperty = new SimpleStringProperty();
  private StringProperty mailContentProperty = new SimpleStringProperty();

  private StringProperty chatToProperty = new SimpleStringProperty();
  private StringProperty chatContentProperty = new SimpleStringProperty();
  private StringProperty filesContentProperty = new SimpleStringProperty();

  private MessagecontainerInfo currentMessageContainer;
  private MessageInfo currentMessage;

  private List<File> files = new ArrayList<File>();

  private BooleanProperty mailEditorVisibleProperty = new SimpleBooleanProperty();
  private BooleanProperty chatEditorVisibleProperty = new SimpleBooleanProperty();

  private ObservableList<MessageInfo> messageInfos = FXCollections.observableArrayList( );




}
