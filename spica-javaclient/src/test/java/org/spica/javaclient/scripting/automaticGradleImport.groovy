package org.spica.javaclient.scripting

import org.spica.javaclient.actions.ActionContext
import org.spica.javaclient.services.Services

ActionContext actionContext = spica


//If page changes, we import the new gradle version into our artifactory
Services services = actionContext.services
boolean siteChanged = services.downloadService.siteChanged("https://gradle.org/releases/");
println "Site changed: " + siteChanged


if (siteChanged) {
    String mailAdress = actionContext.properties.properties.get("spica.mail.smtp.sender")
    println "Sending mail to: " + mailAdress
    //Get all versions from attribute of tag of an http page
    List<String> gradleVersions = services.downloadService.getAttributeOfTags("https://gradle.org/releases/", "a", "name", null)
    services.mailService.sendMail("Spica detected a new gradle version", "Gradle versions: " + gradleVersions.get(0), Arrays.asList(mailAdress))
}
