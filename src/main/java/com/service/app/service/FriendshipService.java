package com.service.app.service;

import com.service.app.entity.Friendship;

import java.util.Optional;

public interface FriendshipService {

    void saveFriendship(Friendship friendship);

    boolean existsFriendship(Long fromId, Long toId);

    Optional<Friendship> findFriendship(Long fromId, Long toId);

    void removeFriendship(Friendship friendship);
}
