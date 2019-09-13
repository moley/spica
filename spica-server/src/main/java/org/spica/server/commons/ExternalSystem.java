package org.spica.server.commons;

public class ExternalSystem {
    private final String url;

    private final String user;

    private final String password;

    public ExternalSystem (final String url, final String user, final String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
