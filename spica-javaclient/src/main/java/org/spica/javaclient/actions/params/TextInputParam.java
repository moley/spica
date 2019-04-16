package org.spica.javaclient.actions.params;

import lombok.Builder;
import lombok.Data;

@Data
public class TextInputParam extends AbstractInputParam {

    @Builder
    public TextInputParam(String key, String displayname, Object value) {
        this.setKey(key);
        this.setDisplayname(displayname);
        this.setValue(value);
    }
}
