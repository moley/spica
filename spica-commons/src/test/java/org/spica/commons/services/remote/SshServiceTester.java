package org.spica.commons.services.remote;

import java.util.Arrays;
import java.util.List;
import org.spica.commons.SpicaProperties;

public class SshServiceTester {

  public static void main(String[] args) {
    SpicaProperties spicaProperties = new SpicaProperties();
    SshService sshService = new SshService();
    List<String> hosts = Arrays.asList(spicaProperties.getValue("spica.ssh.example.host"));
    String user = spicaProperties.getValue("spica.ssh.example.username");
    String password = spicaProperties.getValue("spica.ssh.example.password");
    List<SshResult> results = sshService.execute(spicaProperties, hosts,  user, password, "df -h");
    for (SshResult next: results) {
      System.out.println ("Hostname: " + next.getHostname());
      System.out.println ("Output:   " + next.getOutput());
    }


  }

}
