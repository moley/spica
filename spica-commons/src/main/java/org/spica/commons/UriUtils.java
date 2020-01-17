package org.spica.commons;

import java.net.URI;

public class UriUtils {

    public String removeUser (final String withUser) {
        URI uri = URI.create(withUser);
        if (uri.getPort() >= 0)
          return uri.getScheme() + "://" +  uri.getHost() + ":" + uri.getPort() + uri.getPath();
        else
          return uri.getScheme() + "://" +  uri.getHost() + uri.getPath();
    }
}
