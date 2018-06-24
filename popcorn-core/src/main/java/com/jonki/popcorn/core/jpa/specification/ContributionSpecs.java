package com.jonki.popcorn.core.jpa.specification;

import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.MovieField;
import com.jonki.popcorn.core.jpa.entity.ContributionEntity;
import com.jonki.popcorn.core.jpa.entity.ContributionEntity_;
import com.jonki.popcorn.core.jpa.entity.MovieEntity;

import javax.annotation.Nullable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Specifications for JPA queries.
 *
 * @see <a href="https://docs.spring.io/spring-data/data-jpa/docs/1.0.x/reference/html/#specifications">Docs</a>
 */
public final class ContributionSpecs {

    /**
     * Generate a criteria query predicate for a where clause based on the given parameters.
     *
     * @param root The root to use
     * @param cb The criteria builder to use
     * @param movie The MovieEntity object
     * @param field The movie field
     * @param status The contribution status
     * @param fromDate Creation date range "from"
     * @param toDate Creation date range "to"
     * @return The specification
     */
    public static Predicate getFindPredicate(
            final Root<ContributionEntity> root,
            final CriteriaBuilder cb,
            @Nullable final MovieEntity movie,
            @Nullable final MovieField field,
            @Nullable final DataStatus status,
            @Nullable final Date fromDate,
            @Nullable final Date toDate
    ) {
        final List<Predicate> predicates = new ArrayList<>();

        if (Optional.ofNullable(movie).isPresent()) {
            predicates.add(cb.equal(root.get(ContributionEntity_.movie), movie));
        }
        if (Optional.ofNullable(field).isPresent()) {
            predicates.add(cb.equal(root.get(ContributionEntity_.field), field));
        }
        if (Optional.ofNullable(status).isPresent()) {
            predicates.add(cb.equal(root.get(ContributionEntity_.status), status));
        }
        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(ContributionEntity_.created), fromDate));
        }
        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get(ContributionEntity_.created), toDate));
        }

        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
