package com.service.app.repository;

import com.service.app.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    boolean existsByFromIdAndToId(Long fromId, Long toId);

    Friendship findOneByFromIdAndToId(Long fromId, Long toId);
}
