package org.spica.commons.mail;

import java.io.InputStream;
import lombok.Data;

@Data
public class Attachment {

  private InputStream inputStream;

  private String filename;
}
