package com.service.app.service;

import com.service.app.entity.Friendship;
import com.service.app.entity.User;

import java.util.Optional;

public interface FriendshipService {

    void saveFriendship(Friendship friendship);

    boolean existsFriendship(User fromUser, User toUser);

    Optional<Friendship> findFriendship(User fromUser, User toUser);

    void removeFriendship(Friendship friendship);
}
