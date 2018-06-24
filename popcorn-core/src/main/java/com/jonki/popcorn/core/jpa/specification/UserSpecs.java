package com.jonki.popcorn.core.jpa.specification;

import com.jonki.popcorn.core.jpa.entity.UserEntity;
import com.jonki.popcorn.core.jpa.entity.UserEntity_;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Specifications for JPA queries.
 *
 * @see <a href="https://docs.spring.io/spring-data/data-jpa/docs/1.0.x/reference/html/#specifications">Docs</a>
 */
public final class UserSpecs {

    /**
     * Generate a criteria query predicate for a where clause based on the given parameters.
     *
     * @param root The root to use
     * @param cb The criteria builder to use
     * @param username The user's name
     * @return The specification
     */
    public static Predicate getFindPredicate(
            final Root<UserEntity> root,
            final CriteriaBuilder cb,
            @Nullable final String username
    ) {
        final List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.isTrue(root.get(UserEntity_.enabled)));

        if (StringUtils.isNotBlank(username)) {
            predicates.add(cb.like(cb.upper(root.get(UserEntity_.username)), "%" + username.toUpperCase() + "%"));
        }

        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
