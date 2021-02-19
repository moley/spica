package org.spica.server.software.api;

import io.swagger.annotations.ApiParam;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.KeyValue;
import org.spica.commons.SpicaProperties;
import org.spica.server.software.model.IdAndDisplaynameInfo;
import org.spica.server.software.model.SoftwareInfo;
import org.spica.server.software.service.SoftwareMapper;
import org.spica.server.software.service.SoftwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins="*")
@Data
@Slf4j
public class SoftwareController implements SoftwareApi {

  @Autowired
  private SoftwareService softwareService;

  @Autowired
  private SoftwareMapper softwareMapper;

  private SpicaProperties spicaProperties = new SpicaProperties();

  public ResponseEntity<List<SoftwareInfo>> getSoftware() {
    log.info("call getSoftware");
    return ResponseEntity.of(Optional.of(softwareService.getSoftware()));
  }

  public ResponseEntity<Void> setSoftware(@ApiParam(value = "" ,required=true )  @Valid @RequestBody List<SoftwareInfo> softwareInfo) {
    log.info("call setSoftware with " + softwareInfo.size() + " software items");
    softwareService.setSoftware(softwareInfo);
    return ResponseEntity.ok().build();
  }

  @Override public ResponseEntity<List<IdAndDisplaynameInfo>> getDeployments() {
    log.info("call getDeployments");
    List<KeyValue> keyValuePairs = spicaProperties.getKeyValuePairs("spica.software.deployment");
    return ResponseEntity.ok(softwareMapper.toIdAndDisplaynameInfos(keyValuePairs));
  }

  @Override public ResponseEntity<List<IdAndDisplaynameInfo>> getStates() {
    log.info("call getStates");
    List<KeyValue> keyValuePairs = spicaProperties.getKeyValuePairs("spica.software.state");
    return ResponseEntity.ok(softwareMapper.toIdAndDisplaynameInfos(keyValuePairs));
  }

  @Override public ResponseEntity<List<IdAndDisplaynameInfo>> getTypes() {
    log.info("call getTypes");
    List<KeyValue> keyValuePairs = spicaProperties.getKeyValuePairs("spica.software.type");
    return ResponseEntity.ok(softwareMapper.toIdAndDisplaynameInfos(keyValuePairs));
  }
}
