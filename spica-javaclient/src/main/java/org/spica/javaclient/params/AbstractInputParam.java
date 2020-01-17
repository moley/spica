package org.spica.javaclient.params;

public abstract class AbstractInputParam<T> implements InputParam<T>{

    private String key;

    private String displayname;

    private T value;

    private Renderer<T> renderer;

    private boolean visible = true;

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

    @Override
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
