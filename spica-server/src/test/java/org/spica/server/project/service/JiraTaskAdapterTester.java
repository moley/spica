package org.spica.server.project.service;

import org.spica.server.commons.ExternalSystem;
import org.spica.server.project.model.TaskInfo;

public class JiraTaskAdapterTester {

    public static void main (String [] args) throws InterruptedException {

        String url = args[0];
        String user = args[1];
        String password = args[2];
        String closedIssue = args [3];

        ExternalSystem externalSystem = new ExternalSystem(url, user, password);

        System.out.println ("Url     : " + externalSystem.getUrl());
        System.out.println ("User    : " + externalSystem.getUser());
        System.out.println ("Password: " + externalSystem.getPassword());

        JiraTaskAdapter adapter = new JiraTaskAdapter();
        for (TaskInfo next: adapter.getTasksOfUser(externalSystem)) {
            System.out.println (next.getExternalSystemID() + "-" +next.getExternalSystemKey() + "-" + next.getName());
        }

        String status = adapter.getStatus (externalSystem, closedIssue);
        System.out.println ("Issue " + closedIssue + " has current state " + status);

        if (status.equals("5")) {
            System.out.println ("Set reopened");
            adapter.setStatus(externalSystem, closedIssue, 61);
        }
        else {
            System.out.println ("Set closed");
            adapter.setStatus(externalSystem, closedIssue, 81);
        }


        String statusAfter = adapter.getStatus (externalSystem, closedIssue);
        System.out.println ("Issue " + closedIssue + " has current state " + statusAfter);

    }
}
