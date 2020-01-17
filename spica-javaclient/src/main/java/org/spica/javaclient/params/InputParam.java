package org.spica.javaclient.params;

public interface InputParam<T> {

    String getKey();

    String getDisplayname();

    T getValue();

    void setValue(T value);

    /**
     * false if it should not be set by consoleui, but only by command line parameter
     * @return visible for console ui or not
     */
    boolean isVisible();


}
