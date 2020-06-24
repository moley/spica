package org.spica.server.project.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

	List<Task> findAllByProjectID (final String projectID);

	List<Task> findAllByCurrentUserIDAndExternalSystemID (final String userID, final String externalSystemID);

	Task findByCurrentUserIDAndExternalSystemKey (final String userID, final String externalSystemKey);

}