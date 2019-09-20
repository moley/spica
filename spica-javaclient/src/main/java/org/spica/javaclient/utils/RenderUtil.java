package org.spica.javaclient.utils;

import org.spica.javaclient.model.TopicInfo;
import org.spica.javaclient.model.UserInfo;

public class RenderUtil {

    public String getUser (final UserInfo userinfo) {
        return userinfo.getName() + ", " + userinfo.getFirstname();
    }

    public String getTopic (final TopicInfo topicInfo) {
        if (topicInfo == null)
            return "";

        String asString = "";
        if (topicInfo.getExternalSystemKey() != null)
            asString = topicInfo.getExternalSystemKey() + " - ";

        asString += topicInfo.getName() + "(" + topicInfo.getId() + ")";
        return asString;
    }
}
