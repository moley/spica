package org.spica.javaclient.scripting

import org.spica.javaclient.actions.ActionContext
import org.spica.javaclient.services.Services
import org.spica.commons.services.jenkins.Jenkins

ActionContext actionContext = spica


//If page changes, we import the new gradle version into our artifactory
Services services = actionContext.services
Jenkins jenkins = services.jenkinsService.connectToJenkinsServer();
services.jenkinsService.triggerBuild(jenkins, "sam", "jenkinsPipelineCredentials")

//Trigger build with params
services.jenkinsService.triggerBuild(jenkins, "sam", "jenkinsParameterizedBuildExample", ["VERSION":"99.99"])

