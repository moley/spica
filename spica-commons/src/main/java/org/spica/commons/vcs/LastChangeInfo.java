package org.spica.commons.vcs;

import java.time.LocalDate;

public class LastChangeInfo {

  private LocalDate latestChangeDate;

  private String latestChangeUser;

  public LocalDate getLatestChangeDate() {
    return latestChangeDate;
  }

  public void setLatestChangeDate(LocalDate latestChangeDate) {
    this.latestChangeDate = latestChangeDate;
  }

  public String getLatestChangeUser() {
    return latestChangeUser;
  }

  public void setLatestChangeUser(String latestChangeUser) {
    this.latestChangeUser = latestChangeUser;
  }


}
