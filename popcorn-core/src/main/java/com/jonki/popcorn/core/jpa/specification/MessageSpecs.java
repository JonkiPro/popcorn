package com.jonki.popcorn.core.jpa.specification;

import com.jonki.popcorn.core.jpa.entity.MessageEntity;
import com.jonki.popcorn.core.jpa.entity.MessageEntity_;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.Nullable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Specifications for JPA queries.
 *
 * @see <a href="https://docs.spring.io/spring-data/data-jpa/docs/1.0.x/reference/html/#specifications">Docs</a>
 */
public final class MessageSpecs {

    /**
     * Get a specification using the specified parameters.
     *
     * @param user The sender's user
     * @param subject The subject of the message
     * @param text The text of the message
     * @return The specification
     */
    public static Specification<MessageEntity> findSentMessagesForUser(
            final UserEntity user,
            @Nullable final String subject,
            @Nullable final String text
    ) {
        return (final Root<MessageEntity> root, final CriteriaQuery<?> cq, final CriteriaBuilder cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get(MessageEntity_.sender), user));

            if (Optional.ofNullable(subject).isPresent()) {
                predicates.add(cb.like(cb.upper(root.get(MessageEntity_.subject)), "%" + subject.toUpperCase() + "%"));
            }
            if (Optional.ofNullable(text).isPresent()) {
                predicates.add(cb.like(cb.upper(root.get(MessageEntity_.text)), "%" + text.toUpperCase() + "%"));
            }

            predicates.add(cb.isTrue(root.get(MessageEntity_.isVisibleForSender)));

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    /**
     * Get a specification using the specified parameters.
     *
     * @param user The recipient's user
     * @param subject The subject of the message
     * @param text The text of the message
     * @return The specification
     */
    public static Specification<MessageEntity> findReceivedMessagesForUser(
            final UserEntity user,
            @Nullable final String subject,
            @Nullable final String text
    ) {
        return (final Root<MessageEntity> root, final CriteriaQuery<?> cq, final CriteriaBuilder cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get(MessageEntity_.recipient), user));

            if (Optional.ofNullable(subject).isPresent()) {
                predicates.add(cb.like(cb.upper(root.get(MessageEntity_.subject)), "%" + subject.toUpperCase() + "%"));
            }
            if (Optional.ofNullable(text).isPresent()) {
                predicates.add(cb.like(cb.upper(root.get(MessageEntity_.text)), "%" + text.toUpperCase() + "%"));
            }

            predicates.add(cb.isTrue(root.get(MessageEntity_.isVisibleForRecipient)));

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
