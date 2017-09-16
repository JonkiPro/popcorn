package com.service.app.service.impl;

import com.service.app.entity.Friendship;
import com.service.app.entity.Invitation;
import com.service.app.repository.FriendshipRepository;
import com.service.app.repository.InvitationRepository;
import com.service.app.service.FriendshipService;
import com.service.app.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("friendshipService")
public class FriendshipServiceImpl implements FriendshipService {

    private FriendshipRepository friendshipRepository;

    @Autowired
    private FriendshipServiceImpl(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    @Override
    public void saveFriendship(Friendship friendship) {
        friendshipRepository.save(friendship);
        friendshipRepository
                .save(new Friendship(friendship.getToId(), friendship.getFromId()));
    }

    @Override
    public boolean existsFriendship(Long fromId, Long toId) {
        return friendshipRepository.existsByFromIdAndToId(fromId, toId);
    }

    @Override
    public Optional<Friendship> findFriendship(Long fromId, Long toId) {
        return friendshipRepository.findOneByFromIdAndToId(fromId, toId);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void removeFriendship(Friendship friendship) {
        Long fromId = friendship.getFromId();
        Long toId = friendship.getToId();

        friendshipRepository.delete(this.findFriendship(fromId, toId).get());
        friendshipRepository.delete(this.findFriendship(toId, fromId).get());
    }
}
