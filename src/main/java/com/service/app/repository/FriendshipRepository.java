package com.service.app.repository;

import com.service.app.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    boolean existsByFromIdAndToId(Long fromId, Long toId);

    Optional<Friendship> findOneByFromIdAndToId(Long fromId, Long toId);
}
