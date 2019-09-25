package org.spica.javaclient.actions.navigation;

import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spica.javaclient.actions.Action;
import org.spica.javaclient.actions.ActionContext;
import org.spica.javaclient.actions.ActionGroup;
import org.spica.javaclient.actions.Command;
import org.spica.javaclient.actions.params.InputParams;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class GotoStashAction implements Action {

    private final static Logger LOGGER = LoggerFactory.getLogger(GotoStashAction.class);


    @Override
    public String getDisplayname() {
        return "Goto stash";
    }

    @Override
    public String getDescription() {
        return "Navigates to bitbucket of your current working dir";
    }

    File getNextGitProject (final File currentWorkingDir) {
        if (currentWorkingDir == null)
            return null;

        File currentCheckedDir = currentWorkingDir;
        do {
            if (new File (currentCheckedDir, ".git").exists())
                return currentCheckedDir;
            else if (currentWorkingDir.getParentFile() != null)
                currentCheckedDir = currentWorkingDir.getParentFile();

        } while (currentWorkingDir.getParentFile() != null);
        return null;
    }

    @Override
    public void execute(ActionContext actionContext, InputParams inputParams, String parameterList) {

        File currentWorkingDir = getNextGitProject(actionContext.getCurrentWorkingDir());
        if (currentWorkingDir != null) {
            try {
                Git git = Git.open(currentWorkingDir);
                String remote = git.getRepository().getConfig().getString( "remote", "origin", "url" );

                URL urlRemote = new URL(remote);

                String[] pathTokens = urlRemote.getPath().replace("/scm/", "").split("/");
                String project = pathTokens[0];
                String repo = pathTokens[1].replace(".git", "");
                String branch = git.getRepository().getBranch();

                LOGGER.info("Project : " + project);
                LOGGER.info("Repo    : " + repo);
                LOGGER.info("Branch  : " + branch);

                String projectpath = "/projects/" + project.toUpperCase() + "/repos/" + repo + "/browse?at=refs%2Fheads%2F" + branch;
                String projectUrl = urlRemote.getProtocol() + "://" + urlRemote.getHost() + "/" +  projectpath;
                LOGGER.info("Url " + projectUrl);

                Desktop.getDesktop().browse(new URI(projectUrl));
            } catch (IOException | URISyntaxException e) {
                LOGGER.error(e.getLocalizedMessage(), e);
            }
        }

    }

    @Override
    public ActionGroup getGroup() {
        return ActionGroup.GOTO;
    }

    @Override
    public Command getCommand() {
        return new Command("stash", "s");
    }
}