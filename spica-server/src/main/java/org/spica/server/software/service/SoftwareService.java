package org.spica.server.software.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.spica.commons.filestore.FilestoreItem;
import org.spica.commons.filestore.FilestoreService;
import org.spica.server.software.db.RelationRepository;
import org.spica.server.software.db.SoftwareRepository;
import org.spica.server.software.domain.Relation;
import org.spica.server.software.domain.Software;
import org.spica.server.software.model.RelationInfo;
import org.spica.server.software.model.SoftwareInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
@Slf4j
public class SoftwareService {

  @Autowired
  private SoftwareRepository softwareRepository;

  @Autowired
  private RelationRepository relationRepository;

  @Autowired
  private SoftwareMapper softwareMapper;

  private FilestoreService filestoreService = new FilestoreService();

  public List<SoftwareInfo> getSoftwareTree() {
    List<SoftwareInfo> infos = new ArrayList<>();
    for (Software next: softwareRepository.findByParentIdIsNull()) {
      infos.add(softwareMapper.toSoftwareInfo(next));
    }

    log.info("get software as tree: \n" + infos.size());
    return infos;
  }

  public List<SoftwareInfo> getSoftwareList () {
    List<SoftwareInfo> infos = new ArrayList<>();
    for (Software next: softwareRepository.findAll()) {
      next.setChildren(null);
      infos.add(softwareMapper.toSoftwareInfo(next));
    }

    log.info("get software as list: \n" + infos.size());
    return infos;

  }

  public SoftwareInfo getSoftwareById (final String id) {
    Software software = softwareRepository.findById(id);
    if (software == null)
      throw new IllegalArgumentException("No software found for ID " + id);

    SoftwareInfo softwareInfo = softwareMapper.toSoftwareInfo(software);

    FilestoreItem filestoreItem = filestoreService.objectItem(softwareInfo, FilestoreService.CONTEXT_SCREENSHOT);
    softwareInfo.setScreenshot(filestoreItem.getByteArrayBase64());

    return softwareInfo;
  }

  public void setSoftwareList(final List<SoftwareInfo> software) {
    softwareRepository.deleteAll();

    List<Software> savedSoftware = new ArrayList<>();
    for (SoftwareInfo next: software) {
      savedSoftware.add(softwareMapper.toSoftwareEntity(next));
    }

    softwareRepository.saveAll(savedSoftware);

  }

  public void updateSoftware (final String id, final SoftwareInfo updatedSoftwareInfo) {
    Software updatedSoftware = softwareMapper.toSoftwareEntity(updatedSoftwareInfo);
    softwareRepository.save(updatedSoftware);

  }

  public void setRelationList (final List<RelationInfo> relations) {
    relationRepository.deleteAll();

    List<Relation> savedSoftware = new ArrayList<>();
    for (RelationInfo next: relations) {
      savedSoftware.add(softwareMapper.toRelationEntity(next));
    }
    relationRepository.saveAll(savedSoftware);
  }

  public List<RelationInfo> getRelationsBySoftwareId(String softwareId) {

    Software software = softwareRepository.findById(softwareId);
    Collection<Relation> allBySource = relationRepository.findAllBySource(software);
    List<RelationInfo> relationInfos = new ArrayList<>();
    for (Relation next: allBySource) {
      relationInfos.add(softwareMapper.toRelationInfo(next));
    }

    return relationInfos;
  }

  public void updateRelations (final List<RelationInfo> relationInfos) {
    List<Relation> relations = new ArrayList<>();
    for (RelationInfo next: relationInfos) {
      relations.add(softwareMapper.toRelationEntity(next));
    }
    relationRepository.saveAll(relations);
  }
}
