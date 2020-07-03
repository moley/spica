package org.spica.fx.controllers;

public enum Pages {

  DASHBOARD ("dashboard", "Dashboard", "fa-tachometer", true),
  PLANNING ("planning", "Planning", "fa-calendar", true),
  MESSAGES ("messages", "Messages", "fa-envelope", true),
  MESSAGEDIALOG ("messagedialog", "Dialog", "fa-envelope", false),
  TASKS ("tasks", "Tasks", "fa-tasks", true),
  TASKDETAIL ("taskdetails", "Task Details", "fa-tasks", false),
  PROJECTS ("projects", "Projects", "fa-building", true);

  private String filename;
  private String displayname;
  private String icon;

  private boolean mainAction;

  private Pages (final String filename, final String displayname, final String icon, final boolean mainAction) {
    this.filename = filename;
    this.displayname = displayname;
    this.icon = icon;
    this.mainAction = mainAction;
  }

  public String getFilename() {
    return filename;
  }

  public String getDisplayname() {
    return displayname;
  }

  public String getIcon() {
    return icon;
  }

  public boolean isMainAction() {
    return mainAction;
  }
}
