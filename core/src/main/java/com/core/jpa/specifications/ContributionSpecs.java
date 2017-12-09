package com.core.jpa.specifications;

import com.core.jpa.entity.ContributionEntity;
import com.core.jpa.entity.ContributionEntity_;
import com.core.jpa.entity.MovieEntity;
import com.core.movie.DataStatus;
import com.core.movie.MovieField;
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.Nullable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Specifications for JPA queries.
 */
public class ContributionSpecs {

    /**
     * Generate a specification given the parameters.
     *
     * @param movie The MovieEntity object
     * @param field The movie field
     * @param status The contribution status
     * @param fromDate Creation date range "from"
     * @param toDate Creation date range "to"
     * @return The specification
     */
    public static Specification<ContributionEntity> getFindPredicate(
            @Nullable final MovieEntity movie,
            @Nullable final MovieField field,
            @Nullable final DataStatus status,
            @Nullable final Date fromDate,
            @Nullable final Date toDate
    ) {
        return (final Root<ContributionEntity> root, final CriteriaQuery<?> cq, final CriteriaBuilder cb) -> {
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
                predicates.add(cb.greaterThanOrEqualTo(root.get(ContributionEntity_.creationDate), fromDate));
            }
            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get(ContributionEntity_.creationDate), toDate));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}

