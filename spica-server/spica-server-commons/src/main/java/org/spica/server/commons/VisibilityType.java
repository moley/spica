package org.spica.server.commons;

public enum VisibilityType {
  USER,       //visible from 1..n dedicated users
  USERGROUP,  //visible from 1..n dedicated user
  ROLE,       //visible from 1..n roles
  ALL_USERS,  //visible from all users
  PUBLIC      //visible even from guest users
}
