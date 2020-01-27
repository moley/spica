package org.spica.javaclient.params;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class FlagInputParam<T> extends AbstractInputParam<T> {

    public FlagInputParam(final String key) {
        this.setKey(key);
    }

}
