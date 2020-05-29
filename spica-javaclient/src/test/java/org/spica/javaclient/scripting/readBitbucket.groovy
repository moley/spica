package org.spica.javaclient.scripting

import org.spica.commons.vcs.VcsModuleInfo
import org.spica.commons.vcs.VcsProjectInfo
import org.spica.javaclient.actions.ActionContext
import org.spica.javaclient.services.BitbucketService
import org.spica.javaclient.services.Services

ActionContext actionContext = spica


//If page changes, we import the new gradle version into our artifactory
Services services = actionContext.services
BitbucketService bitbucketService = services.bitbucketService;

bitbucketService.connectToBitbucket();

for (VcsProjectInfo next: bitbucketService.getProjects()) {
    println next.name
    for (VcsModuleInfo nextModule: next.getModules()) {
        println " - " + nextModule.name
    }
}

