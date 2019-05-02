package org.spica.javaclient.actions.params;

public abstract class AbstractInputParam<T> implements InputParam<T>{

    private String key;

    private String displayname;

    private T value;

    private Renderer<T> renderer;

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public Renderer<T> getRenderer() {
        return renderer;
    }

    public void setRenderer(Renderer<T> renderer) {
        this.renderer = renderer;
    }
}
