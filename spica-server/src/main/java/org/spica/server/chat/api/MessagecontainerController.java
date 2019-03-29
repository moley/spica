package org.spica.server.chat.api;


import io.swagger.annotations.ApiParam;
import org.spica.server.chat.domain.Messagecontainer;
import org.spica.server.chat.domain.MessagecontainerMapper;
import org.spica.server.chat.domain.MessagecontainerRepository;
import org.spica.server.chat.model.MessagecontainerInfo;
import org.spica.server.commons.ReferenceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessagecontainerController implements MessagecontainerApi {

  @Autowired
  private MessagecontainerRepository messagecontainerRepository;

  private MessagecontainerMapper messagecontainerMapper = new MessagecontainerMapper();

  @Override
  public ResponseEntity<MessagecontainerInfo> getMessagecontainer(@ApiParam(value = "",required=true) @PathVariable("referenceType") String referenceType, @ApiParam(value = "",required=true) @PathVariable("referenceId") String referenceId) {
    ReferenceType referenceTypeAsEnum = ReferenceType.valueOf(referenceType);
    Messagecontainer messagecontainer = messagecontainerRepository.findByReferenceTypeAndReferenceID(referenceTypeAsEnum, referenceId);
    return new ResponseEntity<MessagecontainerInfo>(messagecontainerMapper.toApi(messagecontainer), HttpStatus.OK);
  }
}
