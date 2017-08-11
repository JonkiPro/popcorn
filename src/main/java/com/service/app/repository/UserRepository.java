package com.service.app.repository;

import com.service.app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findByActivationToken(String activationToken);
    Optional<User> findByEmailChangeToken(String emailChangeToken);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);

    Optional<User> findByUsernameAndEmail(String username, String email);

    List<User> findAll();

    Page<User> findAllByEnabledTrue(Pageable pageable);

    Page<User> findByUsernameContainingAndEnabledTrue(String username, Pageable pageable);

    Long countByUsernameContainingAndEnabledTrue(String username);

    User findOneByUsername(String username);
}
