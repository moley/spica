package org.spica.javaclient.actions.params;

public class ConfirmInputParam extends AbstractInputParam {

    public ConfirmInputParam(String key, String displayname, Object value) {
        this.setKey(key);
        this.setDisplayname(displayname);
        this.setValue(value);
    }
}
