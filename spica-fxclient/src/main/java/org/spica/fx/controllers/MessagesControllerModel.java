package org.spica.fx.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;
import org.spica.javaclient.model.MessagecontainerInfo;

@Data
public class MessagesControllerModel {

  private ObservableList<MessagecontainerInfo> messageContainers = FXCollections.observableArrayList( );

  private StringProperty searchProperty = new SimpleStringProperty();

}
