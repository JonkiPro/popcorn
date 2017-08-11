package com.service.app.service.impl;

import com.service.app.entity.Invitation;
import com.service.app.repository.InvitationRepository;
import com.service.app.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public boolean existsInvitation(Long fromId, Long toId) {
        return invitationRepository.existsByFromIdAndToId(fromId, toId);
    }

    @Override
    public Invitation findInvitation(Long fromId, Long toId) {
        return invitationRepository.findOneByFromIdAndToId(fromId, toId);
    }

    @Override
    public void removeInvitation(Invitation invitation) {
        invitationRepository.delete(invitation);
    }
}
