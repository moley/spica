package org.spica.javaclient.actions.params;

public interface Renderer<T> {
    String toString (T object);
}
