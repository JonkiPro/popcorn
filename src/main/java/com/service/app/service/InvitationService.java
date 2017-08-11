package com.service.app.service;

import com.service.app.entity.Invitation;

public interface InvitationService {

    void saveInvitation(Invitation invitation);

    boolean existsInvitation(Long fromId, Long toId);

    Invitation findInvitation(Long fromId, Long toId);

    void removeInvitation(Invitation invitation);
}
