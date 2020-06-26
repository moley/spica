package org.spica.fx.clipboard;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Clipboard {

  private ObservableList<ClipboardItem> items = FXCollections.observableArrayList(new ArrayList<>());

  public ObservableList<ClipboardItem> getItems() {
    return items;
  }

  public void clear () {
    items.removeAll(items);

  }
}
