package org.spica.javaclient.actions;

public class ActionResult {

  private FoundAction followUpAction;

  private Object userObject;

  public FoundAction getFollowUpAction() {
    return followUpAction;
  }

  public void setFollowUpAction(FoundAction followUpAction) {
    this.followUpAction = followUpAction;
  }

  public Object getUserObject() {
    return userObject;
  }

  public void setUserObject(Object userObject) {
    this.userObject = userObject;
  }
}
