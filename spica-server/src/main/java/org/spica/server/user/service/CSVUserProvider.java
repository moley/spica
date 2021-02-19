package org.spica.server.user.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.SpicaProperties;
import org.spica.server.user.UserRoleName;
import org.spica.server.user.domain.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Slf4j
public class CSVUserProvider implements UserProvider {

  private File leguanHome  = SpicaProperties.getSpicaHome();
  private File configFile;

  public void setUserFile(final File configFile) {
    this.configFile = configFile;
  }

  public File getUserFile( ){
    if (configFile == null) {
      configFile = new File(leguanHome, "userprovider.csv").getAbsoluteFile();
      log.info("configFile = " + configFile.getAbsolutePath());
    }

    if (!configFile.getParentFile().exists())
    configFile.getParentFile().mkdirs();

    if (! configFile.exists()) {
      try {
        configFile.createNewFile();
      } catch (IOException e) {
        throw new IllegalStateException();
      }
    }
    return configFile;

  }

  public List<String> getContent () {
    try {
      return Files.readAllLines(getUserFile().toPath());
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public void clear () {
    getUserFile().delete();
    try {
      getUserFile().createNewFile();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public void addUser (final String username, final String password, final String displayname) {
    List<String> currentUsers = getContent();
    String newUser = username + ":" + password + ":" + displayname;
    currentUsers.add(newUser);
    try {
      Files.write(getUserFile().toPath(), currentUsers);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }

  }

  @Override
  public boolean isLoginNeeded() {
    return true;
  }

  @Override
  public User getUserInfo(String username, String password) {
    for (String next: getContent()) {

      log.info("Check line " + next);
      if (next.startsWith(username + ":")) {
        String [] tokens = next.split(":");
        User userInfo = new User();
        userInfo.setUsername(tokens[0]);
        userInfo.setPassword(tokens[1]);
        userInfo.setDisplayname(tokens[2]);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(UserRoleName.ROLE_USER.name());
        userInfo.setAuthorities(Arrays.asList(authority));

        if (userInfo.getPassword().equals(password)) {
          log.info("Found user for username " + username);
          return userInfo;
        }
      }
    }

    log.warn("Did not find user for username " + username);
    return null;
  }
}
