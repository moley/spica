package org.spica.fx;

import org.spica.fx.clipboard.Clipboard;
import org.spica.fx.clipboard.AttachmentService;

public class ApplicationContext {

  private ScreenManager screenManager = new ScreenManager();

  private Clipboard clipboard = new Clipboard();

  private AttachmentService attachmentService = new AttachmentService();

  public ScreenManager getScreenManager() {
    return screenManager;
  }

  public Clipboard getClipboard() {
    return clipboard;
  }

  public AttachmentService getAttachmentService() {
    return attachmentService;
  }
}
