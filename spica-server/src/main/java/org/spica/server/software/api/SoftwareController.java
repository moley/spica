package org.spica.server.software.api;

import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.KeyValue;
import org.spica.commons.SpicaProperties;
import org.spica.server.software.model.IdAndDisplaynameInfo;
import org.spica.server.software.model.RelationInfo;
import org.spica.server.software.model.SoftwareConstantsInfo;
import org.spica.server.software.model.SoftwareInfo;
import org.spica.server.software.service.SoftwareMapper;
import org.spica.server.software.service.SoftwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
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

  @Override
  public ResponseEntity<List<SoftwareInfo>> getSoftwareList() {
    log.info("call getSoftwareList");
    return ResponseEntity.of(Optional.of(softwareService.getSoftwareTree()));
  }

  @Override
  public ResponseEntity<List<SoftwareInfo>> getOtherSoftwareList(@ApiParam(value = "",required=true) @PathVariable("meId") String meId) {
    log.info("call getOtherSoftwareList");

    List<SoftwareInfo> allSoftware = softwareService.getSoftwareList();
    List<SoftwareInfo> filteredSoftware = allSoftware.stream().filter(line -> ! line.getId().equals(meId)).collect(Collectors.toList());

    return ResponseEntity.of(Optional.of(filteredSoftware));

  }

  @Override
  public ResponseEntity<SoftwareInfo> getSoftwareById(@ApiParam(value = "",required=true) @PathVariable("softwareId") String softwareId) {
    return ResponseEntity.of(Optional.of(softwareService.getSoftwareById(softwareId)));
  }



  @Override
  public ResponseEntity<Void> setSoftwareList(@ApiParam(value = "" ,required=true )  @Valid @RequestBody List<SoftwareInfo> softwareInfo) {
    log.info("call setSoftware with " + softwareInfo.size() + " software items");
    softwareService.setSoftwareList(softwareInfo);
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<Void> updateSoftware(@ApiParam(value = "",required=true) @PathVariable("softwareId") String softwareId,@ApiParam(value = "" ,required=true )  @Valid @RequestBody SoftwareInfo softwareInfo) {
    log.info("call updateSoftware with " + softwareId + "and " + softwareInfo.toString());
    softwareService.updateSoftware(softwareId, softwareInfo);
    return ResponseEntity.ok().build();
  }

  @Override
  public ResponseEntity<SoftwareConstantsInfo> getConstants() {
    log.info("call getConstants");

    SoftwareConstantsInfo softwareConstantsInfo = new SoftwareConstantsInfo();

    List<KeyValue> keyValuePairsDeployments = spicaProperties.getKeyValuePairs("spica.software.deployment");
    softwareConstantsInfo.setDeployments(softwareMapper.toIdAndDisplaynameInfos(keyValuePairsDeployments));

    List<KeyValue> keyValuePairsStates = spicaProperties.getKeyValuePairs("spica.software.state");
    softwareConstantsInfo.setStates(softwareMapper.toIdAndDisplaynameInfos(keyValuePairsStates));

    List<KeyValue> keyValuePairsTypes = spicaProperties.getKeyValuePairs("spica.software.type");
    softwareConstantsInfo.setTypes(softwareMapper.toIdAndDisplaynameInfos(keyValuePairsTypes));

    List<KeyValue> keyValuePairsRelationTypes = spicaProperties.getKeyValuePairs("spica.software.relationtypes");
    softwareConstantsInfo.setRelationtypes(softwareMapper.toIdAndDisplaynameInfos(keyValuePairsRelationTypes));

    List<KeyValue> keyValuePairsRelationGroups = spicaProperties.getKeyValuePairs("spica.software.group");
    softwareConstantsInfo.setGroups(softwareMapper.toIdAndDisplaynameInfos(keyValuePairsRelationGroups));

    List<KeyValue> keyValuePairsRelationRoles = spicaProperties.getKeyValuePairs("spica.software.role");
    softwareConstantsInfo.setRoles(softwareMapper.toIdAndDisplaynameInfos(keyValuePairsRelationRoles));

    return ResponseEntity.ok(softwareConstantsInfo);

  }

  @Override public ResponseEntity<List<RelationInfo>> getRelationsBySoftware(@ApiParam(value = "",required=true) @PathVariable("softwareId") String softwareId) {
    List<RelationInfo> relationsBySoftwareId = softwareService.getRelationsBySoftwareId(softwareId);
    return ResponseEntity.ok(relationsBySoftwareId);
  }

  @Override public ResponseEntity<Void> updateRelations(@ApiParam(value = "" ,required=true )  @Valid @RequestBody List<RelationInfo> relationInfo) {
    log.info("Update relations with " + relationInfo);
    softwareService.updateRelations(relationInfo);
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
