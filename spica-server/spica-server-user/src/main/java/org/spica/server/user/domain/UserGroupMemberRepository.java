package org.spica.server.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupMemberRepository extends JpaRepository<UserGroupMember, Long> {
}
