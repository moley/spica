package org.spica.fx;

import org.spica.fx.clipboard.Clipboard;
import org.spica.fx.clipboard.LinkService;

public class ApplicationContext {

  private ScreenManager screenManager = new ScreenManager();

  private Clipboard clipboard = new Clipboard();

  private LinkService linkService = new LinkService();

  public ScreenManager getScreenManager() {
    return screenManager;
  }

  public Clipboard getClipboard() {
    return clipboard;
  }

  public LinkService getLinkService () {
    return linkService;
  }
}
