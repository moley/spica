package org.spica.javaclient;

import com.sun.jersey.core.util.Base64;
import org.spica.commons.SpicaProperties;
import org.spica.javaclient.api.UserApi;
import org.spica.javaclient.auth.Authentication;
import org.spica.javaclient.auth.HttpBasicAuth;
import org.spica.javaclient.model.UserInfo;

public class ApiTester {

  public static void main(String[] args) {

    String url = "http://localhost:8765/api"; //spicaProperties.getValueNotNull("spica.cli.serverurl");
    String username = "spica"; //spicaProperties.getValueNotNull("spica.cli.username");
    String password = "spica"; //spicaProperties.getValueNotNull("spica.cli.password");
    ApiClient apiClient = new ApiClient();
    apiClient.setBasePath(url);

    String encodedAuth = new String (Base64.encode("spica:spica"));


    apiClient.addDefaultHeader("Authorization", "Basic " + encodedAuth);

    System.out.println (apiClient.getAuthentications());
    System.out.println ("Url        : " + url);
    System.out.println ("Username   : " + username);
    System.out.println ("Password   : " + password);
    UserApi userApi = new UserApi(apiClient);


    try {



      for (UserInfo next: userApi.getUsers()) {
        System.out.println (next.getId() + "-" + next.getUsername() + "-" + next.getDisplayname());
      }
    } catch (ApiException e) {
      System.err.println(e.getCode() + "-" + e.getResponseBody() + "-" + e.getResponseHeaders());
      throw new IllegalStateException(e);
    }
  }
}
