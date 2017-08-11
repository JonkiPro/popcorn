package com.service.app.service.impl;

import com.service.app.entity.Friendship;
import com.service.app.repository.FriendshipRepository;
import com.service.app.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    }

    @Override
    public boolean existsFriendship(Long fromId, Long toId) {
        return friendshipRepository.existsByFromIdAndToId(fromId, toId);
    }

    @Override
    public Friendship findFriendship(Long fromId, Long toId) {
        return friendshipRepository.findOneByFromIdAndToId(fromId, toId);
    }

    @Override
    public void removeFriendship(Friendship friendship) {
        friendshipRepository.delete(friendship);
    }
}
