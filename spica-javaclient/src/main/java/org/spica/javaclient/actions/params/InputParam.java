package org.spica.javaclient.actions.params;

public interface InputParam<T> {

    String getKey();

    String getDisplayname();

    T getValue();


}
