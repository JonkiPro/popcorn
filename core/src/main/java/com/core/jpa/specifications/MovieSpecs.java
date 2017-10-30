package com.core.jpa.specifications;

import com.common.dto.movie.*;
import com.core.jpa.entity.MovieEntity;
import com.core.jpa.entity.MovieEntity_;
import com.core.jpa.entity.movie.*;
import com.core.movie.EditStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Specifications for JPA queries.
 */
public class MovieSpecs {

    /**
     * Generate a specification given the parameters.
     *
     * @param title Title of the movie
     * @param type Type of the movie
     * @param fromDate Date range "from"
     * @param toDate Date range "to"
     * @param countries List of countries
     * @param languages List of languages
     * @param genres List of genres
     * @return The specification
     */
    public static Specification<MovieEntity> getFindPredicate(
            final String title,
            final com.common.dto.movie.MovieType type,
            final Date fromDate,
            final Date toDate,
            final List<CountryType> countries,
            final List<LanguageType> languages,
            final List<GenreType> genres
    ) {
        return (final Root<MovieEntity> root, final CriteriaQuery<?> cq, final CriteriaBuilder cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get(MovieEntity_.status), EditStatus.ACCEPTED));
            if (StringUtils.isNotBlank(title)) {
                predicates.add(cb.like(root.get(MovieEntity_.title), "%" + title + "%"));
            }
            if (Optional.ofNullable(type).isPresent()) {
                predicates.add(cb.equal(root.get(MovieEntity_.type), type));
            }
            if (fromDate != null) {
                final Join<MovieEntity, MovieReleaseDate> listReleaseDates = root.join(MovieEntity_.releaseDates);
                final List<Predicate> orPredicates =
                        Stream.of(fromDate)
                                .map(releaseDate -> cb.greaterThanOrEqualTo(listReleaseDates.get(MovieReleaseDate_.date), fromDate))
                                .collect(Collectors.toList());
                predicates.add(cb.or(orPredicates.toArray(new Predicate[orPredicates.size()])));
            }
            if (toDate != null) {
                final Join<MovieEntity, MovieReleaseDate> listReleaseDates = root.join(MovieEntity_.releaseDates);
                final List<Predicate> orPredicates =
                        Stream.of(toDate)
                                .map(releaseDate -> cb.lessThanOrEqualTo(listReleaseDates.get(MovieReleaseDate_.date), toDate))
                                .collect(Collectors.toList());
                predicates.add(cb.or(orPredicates.toArray(new Predicate[orPredicates.size()])));
            }
            if (countries != null && !countries.isEmpty()) {
                final Join<MovieEntity, MovieCountry> listCountries = root.join(MovieEntity_.countries);
                final List<Predicate> orPredicates =
                        countries
                                .stream()
                                .map(country -> cb.equal(listCountries.get(MovieCountry_.country), country))
                                .collect(Collectors.toList());
                predicates.add(cb.or(orPredicates.toArray(new Predicate[orPredicates.size()])));
            }
            if (languages != null && !languages.isEmpty()) {
                final Join<MovieEntity, MovieLanguage> listLanguages = root.join(MovieEntity_.languages);
                final List<Predicate> orPredicates =
                        languages
                                .stream()
                                .map(language -> cb.equal(listLanguages.get(MovieLanguage_.language), language))
                                .collect(Collectors.toList());
                predicates.add(cb.or(orPredicates.toArray(new Predicate[orPredicates.size()])));
            }
            if (genres != null && !genres.isEmpty()) {
                final Join<MovieEntity, MovieGenre> listGenres = root.join(MovieEntity_.genres);
                final List<Predicate> orPredicates =
                        genres
                                .stream()
                                .map(genre -> cb.equal(listGenres.get(MovieGenre_.genre), genre))
                                .collect(Collectors.toList());
                predicates.add(cb.or(orPredicates.toArray(new Predicate[orPredicates.size()])));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
