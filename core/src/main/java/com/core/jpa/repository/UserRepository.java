package com.core.jpa.repository;

import com.core.jpa.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * User repository.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Find the user by activation token.
     *
     * @param activationToken Activation token
     * @return The user
     */
    Optional<UserEntity> findByActivationToken(String activationToken);

    /**
     * Find the user by email change token.
     *
     * @param emailChangeToken E-mail change token
     * @return The user
     */
    Optional<UserEntity> findByEmailChangeToken(String emailChangeToken);

    /**
     * Find the active user by username.
     *
     * @param username The user's name
     * @return The user
     */
    Optional<UserEntity> findByUsernameIgnoreCaseAndEnabledTrue(String username);

    /**
     * Find the active user by e-mail.
     *
     * @param email The user's e-mail
     * @return The user
     */
    Optional<UserEntity> findByEmailIgnoreCaseAndEnabledTrue(String email);

    /**
     * Find the active user by ID.
     *
     * @param id The user ID
     * @return The user
     */
    Optional<UserEntity> findByIdAndEnabledTrue(Long id);

    /**
     * Find the active user by username and e-mail.
     *
     * @param username The user's name
     * @param email The user's e-mail
     * @return The user
     */
    Optional<UserEntity> findByUsernameIgnoreCaseAndEmailIgnoreCaseAndEnabledTrue(String username, String email);

    /**
     * Find active users.
     *
     * @return Users
     */
    List<UserEntity> findAllByEnabledTrue();

    /**
     * Find active users by pageable.
     *
     * @param pageable PageRequest
     * @return Users
     */
    Page<UserEntity> findAllByEnabledTrue(Pageable pageable);

    /**
     * Find active users by username and pageable.
     *
     * @param username The user's name
     * @param pageable PageRequest
     * @return Users
     */
    Page<UserEntity> findByUsernameContainingIgnoreCaseAndEnabledTrue(String username, Pageable pageable);

    /**
     * Find the active user by username.
     *
     * @param username The user's name
     * @return The user
     */
    UserEntity findOneByUsernameIgnoreCaseAndEnabledTrue(String username);

    /**
     * Count active users by username.
     *
     * @param username The user's name
     * @return Number of active users
     */
    Long countByUsernameContainingIgnoreCaseAndEnabledTrue(String username);

    /**
     * Check whether the user exists by username.
     *
     * @param username The user's name
     * @return True, if the user exists
     */
    boolean existsByUsernameIgnoreCase(String username);

    /**
     * Check whether the user exists by e-mail.
     *
     * @param email The user's e-mail
     * @return True, if the user exists
     */
    boolean existsByEmailIgnoreCase(String email);
}
