import com.google.common.collect.Lists
import org.spica.commons.SpicaProperties
import org.spica.commons.services.remote.SshResult
import org.spica.commons.services.remote.SshService
import org.spica.javaclient.actions.ActionContext

ActionContext actionContext = spica
SpicaProperties spicaProperties = actionContext.properties
SshService sshService = actionContext.services.sshService

String host = spicaProperties.getValue("spica.ssh.example.host")
String username = spicaProperties.getValue("spica.ssh.example.username")
String password = spicaProperties.getValue("spica.ssh.example.password")

List<SshResult> results = sshService.execute(spicaProperties, Lists.asList(host), username, password, "df -h")
println "Results: " + results.size()
for (SshResult next: results) {
    println next.getHostname() + "-" + next.getOutput()
}
