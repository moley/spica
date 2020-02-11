package org.spica.server.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreHistoryRepository extends JpaRepository<ScoreHistory, String> {
}
