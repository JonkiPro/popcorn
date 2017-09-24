package com.service.app.service.impl;

import com.service.app.entity.Friendship;
import com.service.app.entity.User;
import com.service.app.repository.FriendshipRepository;
import com.service.app.service.FriendshipService;
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
                .save(new Friendship(friendship.getToUser(), friendship.getFromUser()));
    }

    @Override
    public boolean existsFriendship(User fromUser, User toUser) {
        return friendshipRepository.existsByFromUserAndToUser(fromUser, toUser);
    }

    @Override
    public Optional<Friendship> findFriendship(User fromUser, User toUser) {
        return friendshipRepository.findOneByFromUserAndToUser(fromUser, toUser);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void removeFriendship(Friendship friendship) {
        User fromUser = friendship.getFromUser();
        User toUser = friendship.getToUser();

        friendshipRepository.delete(this.findFriendship(fromUser, toUser).get());
        friendshipRepository.delete(this.findFriendship(toUser, fromUser).get());
    }
}
