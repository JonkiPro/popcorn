package com.service.app.service;

import com.service.app.entity.Invitation;

import java.util.Optional;

public interface InvitationService {

    void saveInvitation(Invitation invitation);

    boolean existsInvitation(Long fromId, Long toId);

    Optional<Invitation> findInvitation(Long fromId, Long toId);

    void removeInvitation(Invitation invitation);
}
