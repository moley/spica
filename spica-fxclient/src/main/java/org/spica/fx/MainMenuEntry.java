package org.spica.fx;

import com.jfoenix.controls.JFXBadge;
import javafx.scene.control.Button;

public class MainMenuEntry {

  private JFXBadge jfxBadge;

  private Button button;

  public Button getButton() {
    return button;
  }

  public void setButton(Button button) {
    this.button = button;
  }

  public JFXBadge getJfxBadge() {
    return jfxBadge;
  }

  public void setJfxBadge(JFXBadge jfxBadge) {
    this.jfxBadge = jfxBadge;
  }
}
