package com.service.app.repository;

import com.service.app.entity.Friendship;
import com.service.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    boolean existsByFromUserAndToUser(User fromUser, User toUser);

    Optional<Friendship> findOneByFromUserAndToUser(User fromUser, User toUser);
}
