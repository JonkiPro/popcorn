package com.service.app.service.impl;

import com.service.app.entity.Invitation;
import com.service.app.entity.User;
import com.service.app.repository.InvitationRepository;
import com.service.app.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("invitationService")
public class InvitationServiceImpl implements InvitationService {

    private InvitationRepository invitationRepository;

    @Autowired
    public InvitationServiceImpl(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    @Override
    public void saveInvitation(Invitation invitation) {
        invitationRepository.save(invitation);
    }

    @Override
    public boolean existsInvitation(User fromUser, User toUser) {
        return invitationRepository.existsByFromUserAndToUser(fromUser, toUser);
    }

    @Override
    public Optional<Invitation> findInvitation(User fromUser, User toUser) {
        return invitationRepository.findOneByFromUserAndToUser(fromUser, toUser);
    }

    @Override
    public void removeInvitation(Invitation invitation) {
        invitationRepository.delete(invitation);
    }
}
