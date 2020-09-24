package org.spica.commons.services.remote;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Logger;
import com.jcraft.jsch.Session;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jgit.transport.CredentialsProviderUserInfo;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.spica.commons.SpicaProperties;

public class SshService {


  public List<SshResult> execute (SpicaProperties spicaProperties,
                                  List<String> hosts,
                                  final String username, final String password,
                                  final String command) {

    List<SshResult> results = new ArrayList<SshResult>();

    JSch jSch = new JSch();
    for (String nextHost: hosts) {
      try {

        JSch.setLogger(new Logger() {
          @Override public boolean isEnabled(int level) {
            return true;
          }

          @Override public void log(int level, String message) {
            if (level > Logger.INFO)
              System.err.println (message);

          }
        });

        Session session = jSch.getSession(username, nextHost);
        if (username != null && password != null)
          session.setUserInfo(new CredentialsProviderUserInfo(session, new UsernamePasswordCredentialsProvider(username, password)));
        session.setConfig("StrictHostKeyChecking", "no");

        System.out.println ("host     : " + nextHost);
        System.out.println ("user     : " + username);
        System.out.println ("password : " + password);
        System.out.println ("command  : " + command);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();


        session.connect();
        Channel channel = session.openChannel("exec");
        ((ChannelExec)channel).setCommand(command);
        //((ChannelExec)channel).setErrStream(System.err);
        //((ChannelExec)channel).setOutputStream(byteArrayOutputStream);

        InputStream in=channel.getInputStream();

        channel.connect();

        StringBuilder builder = new StringBuilder();


        // read the result from remote server
        byte[] tmp = new byte[1024];
        while (true) {
          while (in.available() > 0) {
            int i = in.read(tmp, 0, 1024);
            if (i < 0) break;
            builder.append(new String(tmp, 0, i));
          }
          if (channel.isClosed()) {
            if (in.available() > 0) continue;
            break;
          }
          try {
            Thread.sleep(1000);
          } catch (Exception ee) {
          }
        }

        channel.disconnect();
        session.disconnect();

        results.add(new SshResult(nextHost, builder.toString()));
      } catch (JSchException | IOException e) {
        throw new RuntimeException(e);
      }
    }

    return results;
  }
}
