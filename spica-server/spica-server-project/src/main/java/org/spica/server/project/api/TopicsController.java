package org.spica.server.project.api;

import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TopicsController implements TopicsApi {
  @Override
  public ResponseEntity<Void> deleteTopic(@ApiParam(value = "",required=true) @PathVariable("topicId") String topicId) {
    return null;
  }
}
