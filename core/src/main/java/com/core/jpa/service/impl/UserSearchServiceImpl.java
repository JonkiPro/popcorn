package com.core.jpa.service.impl;

import com.common.dto.User;
import com.common.exception.ResourceNotFoundException;
import com.core.jpa.entity.UserEntity;
import com.core.jpa.repository.UserRepository;
import com.core.jpa.service.UserSearchService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JPA implementation of the User Search Service.
 */
@Service("userSearchService")
@Slf4j
@Transactional(readOnly = true)
@Validated
public class UserSearchServiceImpl implements UserSearchService {

    private final UserRepository userRepository;

    /**
     * Constructor.
     *
     * @param userRepository The user repository to use
     */
    @Autowired
    public UserSearchServiceImpl(
            @NotNull final UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUserByUsername(
            @NotBlank @Pattern(regexp = "[a-zA-Z0-9_-]{6,36}") final String username
    ) throws ResourceNotFoundException {
        log.info("Called with username {}", username);

        return this.userRepository.findByUsernameIgnoreCaseAndEnabledTrue(username)
                .map(UserEntity::getDTO)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with username " + username));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUserByEmail(
            @NotBlank @Email final String email
    ) throws ResourceNotFoundException {
        log.info("Called with email {}", email);

        return this.userRepository.findByEmailIgnoreCaseAndEnabledTrue(email)
                .map(UserEntity::getDTO)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with e-mail " + email));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getAllUsersByUsername(
            final String username,
            @Min(0) final int page,
            @Min(1) final int pageSize,
            @NotNull final Sort sort
    ) {
        log.info("Called with username {}, page {}, pageSize {}, sort {}", username, page, pageSize, sort);

        return this.userRepository
                .findByUsernameContainingIgnoreCaseAndEnabledTrue(username.trim(), new PageRequest(page, pageSize, sort)).getContent()
                .stream()
                .map(UserEntity::getDTO)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getAllUsers(
            @Min(0) final int page,
            @Min(1) final int pageSize,
            @NotNull final Sort sort
    ) {
        log.info("Called with page {}, pageSize {}, sort {}", page, pageSize, sort);

        return this.userRepository
                .findAllByEnabledTrue(new PageRequest(page, pageSize, sort)).getContent()
                .stream()
                .map(UserEntity::getDTO)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getUserCountByUsername(
            final String username
    ) {
        log.info("Called with username {}", username);

        return this.userRepository.countByUsernameContainingIgnoreCaseAndEnabledTrue(username.trim());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getUserExistsByUsername(
            @NotBlank @Pattern(regexp = "[a-zA-Z0-9_-]{6,36}") final String username
    ) {
        log.info("Called with username {}", username);

        return this.userRepository.existsByUsernameIgnoreCase(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getUserExistsByEmail(
            @NotBlank @Email final String email
    ) {
        log.info("Called with email {}", email);

        return this.userRepository.existsByEmailIgnoreCase(email);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserPassword(
            @NotBlank @Pattern(regexp = "[a-zA-Z0-9_-]{6,36}") final String username
    ) throws ResourceNotFoundException {
        log.info("Called with username {}", username);

        return this.userRepository.findByUsernameIgnoreCaseAndEnabledTrue(username)
                .map(UserEntity::getPassword)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with username " + username));
    }
}
