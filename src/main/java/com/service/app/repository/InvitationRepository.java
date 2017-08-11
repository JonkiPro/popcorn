package com.service.app.repository;

import com.service.app.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    boolean existsByFromIdAndToId(Long fromId, Long toId);

    Invitation findOneByFromIdAndToId(Long fromId, Long toId);
}
