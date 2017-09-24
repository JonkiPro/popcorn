package com.service.app.repository;

import com.service.app.entity.Invitation;
import com.service.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    boolean existsByFromUserAndToUser(User fromUser, User toUser);

    Optional<Invitation> findOneByFromUserAndToUser(User fromUser, User toUser);
}
