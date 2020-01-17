package org.spica.commons.vcs.git;

import com.atlassian.stash.rest.client.api.StashClient;
import com.atlassian.stash.rest.client.api.StashError;
import com.atlassian.stash.rest.client.api.StashRestException;
import com.atlassian.stash.rest.client.api.StashUnauthorizedRestException;
import com.atlassian.stash.rest.client.api.entity.Branch;
import com.atlassian.stash.rest.client.api.entity.Page;
import com.atlassian.stash.rest.client.api.entity.Project;
import com.atlassian.stash.rest.client.api.entity.Repository;
import com.atlassian.stash.rest.client.core.http.HttpMethod;
import com.atlassian.stash.rest.client.core.http.HttpRequest;
import com.atlassian.stash.rest.client.httpclient.HttpClientConfig;
import com.atlassian.stash.rest.client.httpclient.HttpClientHttpExecutor;
import com.atlassian.stash.rest.client.httpclient.HttpClientStashClientFactory;
import com.atlassian.stash.rest.client.httpclient.HttpClientStashClientFactoryImpl;
import com.google.common.io.CharStreams;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.commons.UriUtils;
import org.spica.commons.credentials.PasswordMask;
import org.spica.commons.vcs.VcsBranchInfo;
import org.spica.commons.vcs.DefaultControlAdapter;
import org.spica.commons.vcs.LastChangeInfo;
import org.spica.commons.vcs.VcsModuleInfo;
import org.spica.commons.vcs.VcsProjectInfo;
import org.spica.commons.vcs.VersionControlException;

import static com.atlassian.stash.rest.client.api.StashException.toErrors;
import static com.atlassian.stash.rest.client.core.parser.Parsers.errorsParser;


public class StashAdapter extends DefaultControlAdapter {

  private Logger log = LoggerFactory.getLogger(StashAdapter.class);

  private StashClient client;
  private URL baseurl;
  private String user;
  private String password;

  HttpClientStashClientFactory factory = new HttpClientStashClientFactoryImpl();



  HttpClientHttpExecutor httpClientHttpExecutor;

  private UriUtils uriUtils = new UriUtils();


  @Override
  public void login(final String url, String user, String password) throws VersionControlException {
    this.user = user;
    this.password = password;

    baseurl = null;
    try {
      baseurl = new URL(url);
    } catch (MalformedURLException e) {
      throw new IllegalStateException(e);
    }

    if (client == null) {
      log.info("Login into " + url + " with user " + user + " and password " + password.replaceAll(".", "*"));

      HttpClientConfig clientConfig = new HttpClientConfig(baseurl, user, password);
      httpClientHttpExecutor = new HttpClientHttpExecutor(clientConfig);
      client = factory.getStashClient(clientConfig);
    }
  }

  @Override
  public List<VcsProjectInfo> getProjects() throws VersionControlException {
    log.info("Fetching projects for " + baseurl + " with user " + user + " and password " + password);

    Page<Project> accessibleProjects = null;
    try {
      accessibleProjects = client.getAccessibleProjects(0, 100);  //TODO 100
    } catch (StashUnauthorizedRestException unauthorizedException) {
      throw new VersionControlException("User " + user + " is not authorized to browse " + baseurl + " with configured password");
    }

    List<VcsProjectInfo> projectList = new ArrayList<>();
    for (Project nextStashProject : accessibleProjects.getValues()) {
      log.info("Fetching next project: " + nextStashProject.getName() + "-" + nextStashProject.getSelfUrl());
      VcsProjectInfo newProject = new VcsProjectInfo();
      newProject.setName(nextStashProject.getName());
      newProject.setKey(nextStashProject.getKey());
      projectList.add(newProject);
      if (log.isDebugEnabled()) {
        log.debug("ProjectConf  : " + nextStashProject.getKey());
        log.debug("Personal : " + nextStashProject.isPersonal());
        log.debug("Public  : " + nextStashProject.isPublic());
        log.debug("Type  : " + nextStashProject.getType());
        log.debug("URL  : " + nextStashProject.getSelfUrl());
      }



      Page<Repository> repositories = client.getRepositories(nextStashProject.getName(), null, 0, 1000);
      for (Repository nextrepo : repositories.getValues()) {
        log.info("     Modul: " + nextrepo.getName());
        if (nextrepo.getProject() != null && ! nextrepo.getProject().getName().equalsIgnoreCase(nextStashProject.getName()))
          continue;
        else {

          String url = uriUtils.removeUser(nextrepo.getHttpCloneUrl());
          log.info("Fetching next repo " + nextrepo.getName() + "-" + url);

          VcsModuleInfo module = new VcsModuleInfo(newProject);
          module.setName(nextrepo.getName());
          module.setUrl(url);
          module.setKey(nextrepo.getSlug());
          newProject.getModules().add(module);
        }
      }

    }

    return projectList;
  }


  @Override
  public String getName() {
    return "Stash";
  }

  @Override
  public List<VcsBranchInfo> getBranches(VcsModuleInfo vcsModuleInfo) {
    List<VcsBranchInfo> vcsBranchInfos = new ArrayList<VcsBranchInfo>();
    VcsProjectInfo vcsProjectInfo = vcsModuleInfo.getVcsProjectInfo();

    try {

      Page<Branch> accesibleBranches = client.getRepositoryBranches(vcsProjectInfo.getKey(), vcsModuleInfo.getKey(), null, 0, 1000);
      for (Branch nextbranch : accesibleBranches.getValues()) {
        VcsBranchInfo branchinfo = new VcsBranchInfo(vcsModuleInfo);
        branchinfo.setName(nextbranch.getDisplayId());
        branchinfo.setDefaultBranch(nextbranch.isDefault());
        branchinfo.setLatestChangeset(nextbranch.getLatestChangeset());


        vcsBranchInfos.add(branchinfo);
      }
    } catch (StashRestException e) {
      log.error("Error reading " + vcsProjectInfo.getKey() +" - " + vcsModuleInfo.getKey(), e);
    }


    return vcsBranchInfos;
  }

  public LastChangeInfo getLastChangeInfo (VcsBranchInfo vcsBranchInfo) {

    LastChangeInfo lastChangeInfo = new LastChangeInfo();

    VcsModuleInfo vcsModuleInfo = vcsBranchInfo.getVcsModuleInfo();
    VcsProjectInfo vcsProjectInfo = vcsModuleInfo.getVcsProjectInfo();

    final String lastCommitUrl = "/rest/api/latest/projects/" + vcsProjectInfo.getKey() + "/repos/" + vcsModuleInfo.getKey() + "/commits?until=" + vcsBranchInfo
        .getName() + "&limit=0&start=0";

    log.info("Fetching last change info in branch " + vcsBranchInfo.getName() + " from " + lastCommitUrl);
    HttpRequest httpRequest = new HttpRequest(lastCommitUrl, HttpMethod.GET, null, false);

    httpClientHttpExecutor.execute(httpRequest, response -> {
      String responseString = null;
      if (response.getBodyStream() != null) {
        responseString = CharStreams.toString(new BufferedReader(response.getBodyReader(StandardCharsets.UTF_8.name())));
      }
      if (log.isTraceEnabled()) {
        log.trace(String.format("doRestCall response: code=%d; response='%s'", response.getStatusCode(), responseString));
      }

      if (response.isSuccessful()) {
        if (responseString != null) {
          try {
            JsonElement jsonElement = new JsonParser().parse(responseString);

            if (jsonElement != null) {
              Long timestamp = ((JsonObject) ((JsonObject) jsonElement).get("values").getAsJsonArray().get(0)).get("authorTimestamp").getAsLong();
              String user = ((JsonObject)((JsonObject)((JsonObject) jsonElement).get("values").getAsJsonArray().get(0)).get("author")).get("name").getAsString();
              Date date = new Date (timestamp);
              lastChangeInfo.setLatestChangeDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
              lastChangeInfo.setLatestChangeUser(user);
              log.info("Got last change info " + lastChangeInfo.getLatestChangeDate() + "-" + lastChangeInfo.getLatestChangeUser());
              return lastChangeInfo;
            }
            else
              log.info("jsonElement is null");

          } catch (JsonSyntaxException e) {
            throw new IllegalStateException("Error on request: " + e.getLocalizedMessage() , e);
          }
        }
        else
          log.warn("ResponseString is null");
      } else {
        List<StashError> errors;
        try {
          if (responseString != null) {
            JsonElement jsonElement = new JsonParser().parse(responseString);
            if (jsonElement != null && jsonElement.isJsonObject()) {
              errors = errorsParser().apply(jsonElement);
            } else {
              errors = toErrors("Request to Stash failed. Returned with " + response.getStatusCode() + ". Response body is empty.");
            }
          } else {
            errors = new ArrayList<>();
          }
        } catch (JsonSyntaxException entityException) {
          errors = toErrors("Request to Stash failed. Returned with " + response.getStatusCode());
        }
        log.warn("Error on fetching latest commit infos of " + vcsProjectInfo.getKey() + "-" + vcsModuleInfo.getKey() + "-" + vcsBranchInfo
            .getName() + "-" + user + "-" + password + ":" + errors);
      }

      return lastChangeInfo;

    });

    return lastChangeInfo;
  }

  public void checkout(VcsBranchInfo branch, File targetPath) {
    String url = branch.getVcsModuleInfo().getUrl();


    try {
      PasswordMask passwordMask = new PasswordMask();
      String maskedPassword = passwordMask.getMaskedPassword(password);

      log.info("Checkout branch " + branch.getName() + " to path " + targetPath.getAbsoluteFile() + " for user " + user + " and password " + maskedPassword);


      CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(user, password);
      Git git = Git.cloneRepository().setCredentialsProvider(credentialsProvider).setDirectory(targetPath).setBranch(branch.getName()).setURI(url).call();
      log.info(git.status().call().isClean() + " -is clean");

      git.close();
    } catch (Exception e) {
      throw new IllegalStateException("Error when cloning " + url + ":" + e.getLocalizedMessage(), e);
    }

  }

  public void setHttpClientHttpExecutor(HttpClientHttpExecutor httpClientHttpExecutor) {
    this.httpClientHttpExecutor = httpClientHttpExecutor;
  }


}
