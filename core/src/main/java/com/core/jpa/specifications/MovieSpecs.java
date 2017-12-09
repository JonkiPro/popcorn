package com.core.jpa.specifications;

import com.common.dto.movie.type.CountryType;
import com.common.dto.movie.type.GenreType;
import com.common.dto.movie.type.LanguageType;
import com.common.dto.movie.type.MovieType;
import com.core.jpa.entity.MovieEntity;
import com.core.jpa.entity.MovieEntity_;
import com.core.jpa.entity.movie.*;
import com.core.movie.DataStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.annotation.Nullable;
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
     * @param minRating Minimal rating
     * @param maxRating Maximum rating
     * @return The specification
     */
    public static Specification<MovieEntity> getFindPredicate(
            @Nullable final String title,
            @Nullable final MovieType type,
            @Nullable final Date fromDate,
            @Nullable final Date toDate,
            @Nullable final List<CountryType> countries,
            @Nullable final List<LanguageType> languages,
            @Nullable final List<GenreType> genres,
            @Nullable final Integer minRating,
            @Nullable final Integer maxRating
    ) {
        return (final Root<MovieEntity> root, final CriteriaQuery<?> cq, final CriteriaBuilder cb) -> {
            final List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get(MovieEntity_.status), DataStatus.ACCEPTED));
            if (StringUtils.isNotBlank(title)) {
                predicates.add(cb.like(root.get(MovieEntity_.title), "%" + title + "%"));
            }
            if (Optional.ofNullable(type).isPresent()) {
                predicates.add(cb.equal(root.get(MovieEntity_.type), type));
            }
            if (fromDate != null) {
                final Join<MovieEntity, MovieReleaseDateEntity> listReleaseDates = root.join(MovieEntity_.releaseDates);
                final List<Predicate> orPredicates =
                        Stream.of(fromDate)
                                .map(releaseDate -> cb.greaterThanOrEqualTo(listReleaseDates.get(MovieReleaseDateEntity_.date), fromDate))
                                .collect(Collectors.toList());
                predicates.add(cb.or(orPredicates.toArray(new Predicate[orPredicates.size()])));
            }
            if (toDate != null) {
                final Join<MovieEntity, MovieReleaseDateEntity> listReleaseDates = root.join(MovieEntity_.releaseDates);
                final List<Predicate> orPredicates =
                        Stream.of(toDate)
                                .map(releaseDate -> cb.lessThanOrEqualTo(listReleaseDates.get(MovieReleaseDateEntity_.date), toDate))
                                .collect(Collectors.toList());
                predicates.add(cb.or(orPredicates.toArray(new Predicate[orPredicates.size()])));
            }
            if (countries != null && !countries.isEmpty()) {
                final Join<MovieEntity, MovieCountryEntity> listCountries = root.join(MovieEntity_.countries);
                final List<Predicate> orPredicates =
                        countries
                                .stream()
                                .map(country -> cb.equal(listCountries.get(MovieCountryEntity_.country), country))
                                .collect(Collectors.toList());
                predicates.add(cb.or(orPredicates.toArray(new Predicate[orPredicates.size()])));
            }
            if (languages != null && !languages.isEmpty()) {
                final Join<MovieEntity, MovieLanguageEntity> listLanguages = root.join(MovieEntity_.languages);
                final List<Predicate> orPredicates =
                        languages
                                .stream()
                                .map(language -> cb.equal(listLanguages.get(MovieLanguageEntity_.language), language))
                                .collect(Collectors.toList());
                predicates.add(cb.or(orPredicates.toArray(new Predicate[orPredicates.size()])));
            }
            if (genres != null && !genres.isEmpty()) {
                final Join<MovieEntity, MovieGenreEntity> listGenres = root.join(MovieEntity_.genres);
                final List<Predicate> orPredicates =
                        genres
                                .stream()
                                .map(genre -> cb.equal(listGenres.get(MovieGenreEntity_.genre), genre))
                                .collect(Collectors.toList());
                predicates.add(cb.or(orPredicates.toArray(new Predicate[orPredicates.size()])));
            }
            if (minRating != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get(MovieEntity_.rating), (float) minRating));
            }
            if (maxRating != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get(MovieEntity_.rating), (float) maxRating));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
