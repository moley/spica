package org.spica.fx;

import org.kordamp.ikonli.javafx.FontIcon;

public class Consts {

  public final static int ICONSIZE_MENU = 50;

  public final static FontIcon createIcon (String name, int iconSize) {
    FontIcon fontIcon =  new FontIcon(name);
    fontIcon.setIconSize(iconSize);
    return fontIcon;
  }
}
