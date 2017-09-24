package com.service.app.service;

import com.service.app.entity.Invitation;
import com.service.app.entity.User;

import java.util.Optional;

public interface InvitationService {

    void saveInvitation(Invitation invitation);

    boolean existsInvitation(User fromUser, User toUser);

    Optional<Invitation> findInvitation(User fromUser, User toUser);

    void removeInvitation(Invitation invitation);
}
