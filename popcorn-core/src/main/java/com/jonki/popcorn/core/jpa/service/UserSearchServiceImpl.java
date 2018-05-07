package com.jonki.popcorn.core.jpa.service;

import com.jonki.popcorn.common.dto.RelationshipStatus;
import com.jonki.popcorn.common.dto.User;
import com.jonki.popcorn.common.dto.search.UserSearchResult;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import com.jonki.popcorn.core.jpa.entity.UserEntity_;
import com.jonki.popcorn.core.jpa.repository.UserRepository;
import com.jonki.popcorn.core.service.AuthorizationService;
import com.jonki.popcorn.core.service.UserSearchService;
import com.jonki.popcorn.core.jpa.specification.UserSpecs;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
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
    private final AuthorizationService authorizationService;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Constructor.
     *
     * @param userRepository The user repository to use
     * @param authorizationService The authorization service to use
     */
    @Autowired
    public UserSearchServiceImpl(
            @NotNull final UserRepository userRepository,
            @NotNull final AuthorizationService authorizationService
    ) {
        this.userRepository = userRepository;
        this.authorizationService = authorizationService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<UserSearchResult> findUsers(
            @Nullable final String username,
            @NotNull final Pageable page
    ) {
        log.info("Called with username {}, page {}", username, page);

        final CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        final Root<UserEntity> root = countQuery.from(UserEntity.class);

        final Predicate whereClause = UserSpecs
                .getFindPredicate(
                        root,
                        cb,
                        username
                );

        countQuery.select(cb.count(root)).where(whereClause);

        final Long count = this.entityManager.createQuery(countQuery).getSingleResult();

        if (count > 0) {
            final CriteriaQuery<UserSearchResult> contentQuery = cb.createQuery(UserSearchResult.class);
            contentQuery.from(UserEntity.class);

            contentQuery.multiselect(
                    root.get(UserEntity_.uniqueId),
                    root.get(UserEntity_.username),
                    root.get(UserEntity_.email)
            );

            contentQuery.where(whereClause);

            final Sort sort = page.getSort();
            final List<Order> orders = new ArrayList<>();
            sort.iterator().forEachRemaining(
                    order -> {
                        if (order.isAscending()) {
                            orders.add(cb.asc(root.get(order.getProperty())));
                        } else {
                            orders.add(cb.desc(root.get(order.getProperty())));
                        }
                    }
            );

            contentQuery.orderBy(orders);

            final List<UserSearchResult> results = this.entityManager
                    .createQuery(contentQuery)
                    .setFirstResult(((Long) page.getOffset()).intValue())
                    .setMaxResults(page.getPageSize())
                    .getResultList();

            return new PageImpl<>(results, page, count);
        } else {
            return new PageImpl<>(Lists.newArrayList(), page, count);
        }
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
                .map(ServiceUtils::toUserDto)
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
                .map(ServiceUtils::toUserDto)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with e-mail " + email));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsUserByUsername(
            @NotBlank @Pattern(regexp = "[a-zA-Z0-9_-]{6,36}") final String username
    ) {
        log.info("Called with username {}", username);

        return this.userRepository.existsByUsernameIgnoreCase(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsUserByEmail(
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getFriends(
            @NotBlank final String id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return this.findUser(id).getFriends()
                .stream()
                .map(ServiceUtils::toUserDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> getInvitations(
            @NotNull final Boolean outgoing
    ) throws ResourceNotFoundException {
        log.info("Called with outgoing {}", outgoing);

        final String id = this.authorizationService.getUserId();

        if(outgoing) {
            return this.findUser(id).getSentInvitations()
                    .stream()
                    .map(ServiceUtils::toUserDto)
                    .collect(Collectors.toList());
        } else {
            return this.findUser(id).getReceivedInvitations()
                    .stream()
                    .map(ServiceUtils::toUserDto)
                    .collect(Collectors.toList());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RelationshipStatus getUserFriendStatus(
            @NotBlank final String id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        final RelationshipStatus status;
        final UserEntity authUser = this.findUser(this.authorizationService.getUserId());
        final UserEntity toUser = this.findUser(id);

        if (authUser.getFriends().contains(toUser)) {
            status = RelationshipStatus.FRIEND;
        } else if (authUser.getSentInvitations().contains(toUser)) {
            status = RelationshipStatus.INVITATION_FROM_YOU;
        } else if (toUser.getSentInvitations().contains(authUser)) {
            status = RelationshipStatus.INVITATION_TO_YOU;
        } else {
            status = RelationshipStatus.UNKNOWN;
        }

        return status;
    }

    /**
     * Helper method to find the user entity.
     *
     * @param id The user ID
     * @return The user
     * @throws ResourceNotFoundException if no user found
     */
    private UserEntity findUser(final String id) throws ResourceNotFoundException {
        return this.userRepository
                .findByUniqueIdAndEnabledTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with id " + id));
    }
}
