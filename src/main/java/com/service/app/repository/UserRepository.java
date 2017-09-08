package com.service.app.repository;

import com.service.app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsernameIgnoreCase(String username);
    boolean existsByEmailIgnoreCase(String email);

    Optional<User> findByActivationToken(String activationToken);
    Optional<User> findByEmailChangeToken(String emailChangeToken);
    Optional<User> findByUsernameIgnoreCase(String username);
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findById(Long id);

    Optional<User> findByUsernameIgnoreCaseAndEmailIgnoreCase(String username, String email);

    List<User> findAll();

    Page<User> findAllByEnabledTrue(Pageable pageable);

    Page<User> findByUsernameContainingIgnoreCaseAndEnabledTrue(String username, Pageable pageable);

    Long countByUsernameContainingIgnoreCaseAndEnabledTrue(String username);

    User findOneByUsernameIgnoreCase(String username);
}
