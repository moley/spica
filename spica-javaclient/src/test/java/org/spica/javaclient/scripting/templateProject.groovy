import org.spica.commons.SpicaProperties
import org.spica.javaclient.actions.ActionContext
import org.spica.javaclient.params.ConfirmInputParam
import org.spica.javaclient.params.InputParamGroup
import org.spica.javaclient.params.InputParams
import org.spica.javaclient.params.TextInputParam
import org.spica.javaclient.services.Services

ActionContext actionContext = spica
SpicaProperties spicaProperties = actionContext.properties
Services services = actionContext.services

InputParams inputParams = new InputParams()
InputParamGroup inputParamGroup = new InputParamGroup()
inputParamGroup.inputParams.add(new TextInputParam(1, 'name', "Name"))
inputParamGroup.inputParams.add(new ConfirmInputParam('db', "Needs database", null))
inputParams.inputParamGroups.add(inputParamGroup);
inputParams = actionContext.actionParamFactory.build(actionContext, inputParams)

System.out.println ("Name was set to " + inputParams.getInputParam('name').getValue())
System.out.println ("Db was set to " + inputParams.getInputParam('db').getValue())
System.out.println ("Ui was set to " + inputParams.getInputParam('ui').getValue())
