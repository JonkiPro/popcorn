package com.core.jpa.repository;

import com.core.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

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
    Optional<UserEntity> findByActivationToken(final String activationToken);

    /**
     * Find the user by email change token.
     *
     * @param emailChangeToken E-mail change token
     * @return The user
     */
    Optional<UserEntity> findByEmailChangeToken(final String emailChangeToken);

    /**
     * Find the active user by username.
     *
     * @param username The user's name
     * @return The user
     */
    Optional<UserEntity> findByUsernameIgnoreCaseAndEnabledTrue(final String username);

    /**
     * Find the active user by e-mail.
     *
     * @param email The user's e-mail
     * @return The user
     */
    Optional<UserEntity> findByEmailIgnoreCaseAndEnabledTrue(final String email);

    /**
     * Find the active user by ID.
     *
     * @param id The user ID
     * @return The user
     */
    Optional<UserEntity> findByUniqueIdAndEnabledTrue(final String id);

    /**
     * Find the active user by username and e-mail.
     *
     * @param username The user's name
     * @param email The user's e-mail
     * @return The user
     */
    Optional<UserEntity> findByUsernameIgnoreCaseAndEmailIgnoreCaseAndEnabledTrue(final String username, final String email);

    /**
     * Find the active user by username.
     *
     * @param username The user's name
     * @return The user
     */
    UserEntity findOneByUsernameIgnoreCaseAndEnabledTrue(final String username);

    /**
     * Check whether the user exists by username.
     *
     * @param username The user's name
     * @return True, if the user exists
     */
    boolean existsByUsernameIgnoreCase(final String username);

    /**
     * Check whether the user exists by e-mail.
     *
     * @param email The user's e-mail
     * @return True, if the user exists
     */
    boolean existsByEmailIgnoreCase(final String email);
}
