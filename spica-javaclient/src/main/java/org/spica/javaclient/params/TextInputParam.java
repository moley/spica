package org.spica.javaclient.params;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data @EqualsAndHashCode(callSuper = false) public class TextInputParam extends AbstractInputParam {

  private int numberOfLines = 1;

  public TextInputParam(int numberOfLines, String key, String displayname) {
    this(numberOfLines, key, displayname, null);
  }

  public TextInputParam(int numberOfLines, String key, String displayname, Object value) {
    this.setNumberOfLines(numberOfLines);
    this.setKey(key);
    this.setDisplayname(displayname);
    this.setValue(value);
  }
}
