package org.spica.fx.clipboard;

import java.io.File;
import java.util.UUID;
import org.spica.commons.SpicaProperties;

public class LinkService {

  public String createLinkId () {
    return UUID.randomUUID().toString();
  }

  public File createLinkFile () {
    File spicaHome = SpicaProperties.getSpicaHome();
    File linksFile = new File (spicaHome, "links");
    linksFile.mkdirs();
    return new File (linksFile, createLinkId());
  }
}
