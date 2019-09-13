package org.spica.server.project.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, String> {

	List<Topic> findAllByProjectID (final String projectID);

	List<Topic> findAllByCurrentUserIDAndExternalSystemID (final String userID, final String externalSystemID);

	Topic findByCurrentUserIDAndExternalSystemKey (final String userID, final String externalSystemKey);

}