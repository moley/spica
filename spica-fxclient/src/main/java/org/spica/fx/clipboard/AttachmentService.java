package org.spica.fx.clipboard;

import java.io.File;
import java.util.UUID;
import org.spica.commons.SpicaProperties;

public class AttachmentService {

  public String createAttachmentID() {
    return UUID.randomUUID().toString();
  }

  public File createAttachment() {
    File spicaHome = SpicaProperties.getSpicaHome();
    File linksFile = new File (spicaHome, "attachments");
    linksFile.mkdirs();
    return new File (linksFile, createAttachmentID());
  }
}
