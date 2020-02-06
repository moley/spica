package org.spica.fx;

public class UserView extends AbstractView {

  @Override public String getId() {
    return "users";
  }

  @Override public String getDisplayname() {
    return "Users";
  }


  @Override public String getIcon() {
    return "fa-user";
  }
}
