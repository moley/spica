package org.spica.server.admin.api;

import io.swagger.annotations.ApiParam;
import java.util.Map;
import java.util.Properties;
import javax.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.PropertiesUtils;
import org.spica.commons.SpicaProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins="*")
@Data
@Slf4j
public class AdminController implements AdminApi{

  private PropertiesUtils propertiesUtils = new PropertiesUtils();

  @Override
  public ResponseEntity<Map<String, String>> getCustomProperties() {
    SpicaProperties spicaProperties = new SpicaProperties();
    Properties properties = spicaProperties.getCustomProperties();
    return ResponseEntity.ok(propertiesUtils.convert2HashMap(properties));
  }

  @Override
  public ResponseEntity<Void> setCustomProperties(@ApiParam(value = "" ,required=true )  @Valid @RequestBody Map<String, String> requestBody) {
    SpicaProperties spicaProperties = new SpicaProperties();
    spicaProperties.saveCustomProperties(propertiesUtils.convert2Properties(requestBody));
    return ResponseEntity.ok().build();
  }
}
