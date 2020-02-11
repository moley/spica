package org.spica.fx;

public class MeView extends AbstractView {
  @Override public String getId() {
    return "me";
  }

  @Override public String getDisplayname() {
    return "Me";
  }


  @Override public String getIcon() {
    return "fa-user";
  }
}
