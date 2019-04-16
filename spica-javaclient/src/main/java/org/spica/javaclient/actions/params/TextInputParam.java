package org.spica.javaclient.actions.params;

import lombok.Builder;
import lombok.Data;

@Data
public class TextInputParam extends AbstractInputParam {

    private int numberOfLines = 1;

    @Builder
    public TextInputParam(int numberOfLines, String key, String displayname, Object value) {
        this.setNumberOfLines(numberOfLines);
        this.setKey(key);
        this.setDisplayname(displayname);
        this.setValue(value);
    }
}
