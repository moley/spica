package org.spica.javaclient.utils;

import org.spica.javaclient.model.LinkInfo;
import org.spica.javaclient.model.ProjectInfo;
import org.spica.javaclient.model.TaskInfo;
import org.spica.javaclient.model.UserInfo;

public class RenderUtil {

    public String getUser(final UserInfo userinfo) {
        if (userinfo == null)
            return "unknown user";
        else
          return userinfo.getName() + ", " + userinfo.getFirstname();
    }

    public String getTask(final TaskInfo taskInfo) {
        if (taskInfo == null)
            return "";

        String asString = "";
        if (taskInfo.getExternalSystemKey() != null)
            asString = taskInfo.getExternalSystemKey() + " - ";

        asString += taskInfo.getName() + "(" + taskInfo.getId() + ")";
        return asString;
    }

    public String getProject(final ProjectInfo projectInfo) {
        if (projectInfo == null)
            return "";
        return projectInfo.getName() + "(" + projectInfo.getId() + ")";
    }

    public String trimOnDemand(final String fullString, final int length) {
        if (fullString.length() > length)
            return fullString.substring(0, length) + "...";
        else
            return fullString;

    }



    public String getLink(final LinkInfo linkInfo) {
        String trimmedName = trimOnDemand(linkInfo.getName(), 40);
        String trimmedUrl = trimOnDemand(linkInfo.getUrl(), 70);
        String type = linkInfo.getType().name();
        String linkToken = String.format("  %-10s %-50s%-80s (%s)", type, trimmedName, trimmedUrl, linkInfo.getId());
        return linkToken;
    }
}
