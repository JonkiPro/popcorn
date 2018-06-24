package com.jonki.popcorn.core.jpa.specification;

import com.google.common.collect.Lists;
import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.movie.type.CountryType;
import com.jonki.popcorn.common.dto.movie.type.GenreType;
import com.jonki.popcorn.common.dto.movie.type.LanguageType;
import com.jonki.popcorn.common.dto.movie.type.MovieType;
import com.jonki.popcorn.core.jpa.entity.MovieEntity;
import com.jonki.popcorn.core.jpa.entity.MovieEntity_;
import com.jonki.popcorn.core.jpa.entity.movie.MovieCountryEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieCountryEntity_;
import com.jonki.popcorn.core.jpa.entity.movie.MovieGenreEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieGenreEntity_;
import com.jonki.popcorn.core.jpa.entity.movie.MovieLanguageEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieLanguageEntity_;
import com.jonki.popcorn.core.jpa.entity.movie.MovieOtherTitleEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieOtherTitleEntity_;
import com.jonki.popcorn.core.jpa.entity.movie.MovieReleaseDateEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieReleaseDateEntity_;
import com.jonki.popcorn.test.category.UnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 * Tests for the movie specifications.
 */
@Category(UnitTest.class)
public class MovieSpecsUnitTests {

    private static final String TITLE = "JonkiMovie";
    private static final MovieType TYPE = MovieType.CINEMA;
    private static final Date FROM_DATE = new Date();
    private static final Date TO_DATE = new Date();
    private static final List<CountryType> COUNTRIES = Lists.newArrayList(CountryType.USA);
    private static final List<LanguageType> LANGUAGES = Lists.newArrayList(LanguageType.ENGLISH);
    private static final List<GenreType> GENRES = Lists.newArrayList(GenreType.ACTION);
    private static final Integer MIN_RATING = 7;
    private static final Integer MAX_RATING = 10;

    private Root<MovieEntity> movieRoot;
    private CriteriaBuilder cb;
    private ListJoin<MovieEntity, MovieOtherTitleEntity> otherTitleEntityJoin;
    private ListJoin<MovieEntity, MovieReleaseDateEntity> releaseDateEntityJoin;
    private ListJoin<MovieEntity, MovieCountryEntity> countryEntityJoin;
    private ListJoin<MovieEntity, MovieLanguageEntity> languageEntityJoin;
    private ListJoin<MovieEntity, MovieGenreEntity> genreEntityJoin;

    /**
     * Setup some variables.
     */
    @Before
    @SuppressWarnings("unchecked")
    public void setup() {
        this.movieRoot = (Root<MovieEntity>) Mockito.mock(Root.class);
        final Root<MovieOtherTitleEntity> movieOtherTitleRoot = (Root<MovieOtherTitleEntity>) Mockito.mock(Root.class);
        final Root<MovieReleaseDateEntity> movieReleaseDateEntityRoot = (Root<MovieReleaseDateEntity>) Mockito.mock(Root.class);
        final Root<MovieCountryEntity> movieCountryEntityRoot = (Root<MovieCountryEntity>) Mockito.mock(Root.class);
        final Root<MovieLanguageEntity> movieLanguageEntityRoot = (Root<MovieLanguageEntity>) Mockito.mock(Root.class);
        final Root<MovieGenreEntity> movieGenreEntityRoot = (Root<MovieGenreEntity>) Mockito.mock(Root.class);
        this.cb = Mockito.mock(CriteriaBuilder.class);

        final Path<String> titlePath = (Path<String>) Mockito.mock(Path.class);
        final Path<DataStatus> statusPath = (Path<DataStatus>) Mockito.mock(Path.class);
        this.otherTitleEntityJoin = (ListJoin<MovieEntity, MovieOtherTitleEntity>) Mockito.mock(ListJoin.class);
        Mockito.when(movieOtherTitleRoot.get(MovieOtherTitleEntity_.title)).thenReturn(titlePath);
        Mockito.when(movieOtherTitleRoot.get(MovieOtherTitleEntity_.status)).thenReturn(statusPath);
        Mockito.when(this.movieRoot.join(MovieEntity_.otherTitles)).thenReturn(this.otherTitleEntityJoin);
        final Predicate otherTitleInPredicate = Mockito.mock(Predicate.class);
        Mockito
                .when(
                        this.cb.and(
                                this.cb.like(this.cb.upper(titlePath), "%" + TITLE.toUpperCase() + "%"),
                                this.cb.equal(otherTitleEntityJoin.get(MovieOtherTitleEntity_.status), DataStatus.ACCEPTED)
                        )
                ).thenReturn(otherTitleInPredicate);

        final Path<MovieType> typePath = (Path<MovieType>) Mockito.mock(Path.class);
        final Predicate equalTypePredicate = Mockito.mock(Predicate.class);
        Mockito.when(this.movieRoot.get(MovieEntity_.type)).thenReturn(typePath);
        Mockito.when(this.cb.equal(typePath, TYPE)).thenReturn(equalTypePredicate);

        final Path<Date> fromDatePath = (Path<Date>) Mockito.mock(Path.class);
        this.releaseDateEntityJoin = (ListJoin<MovieEntity, MovieReleaseDateEntity>) Mockito.mock(ListJoin.class);
        Mockito.when(movieReleaseDateEntityRoot.get(MovieReleaseDateEntity_.date)).thenReturn(fromDatePath);
        Mockito.when(movieReleaseDateEntityRoot.get(MovieReleaseDateEntity_.status)).thenReturn(statusPath);
        Mockito.when(this.movieRoot.join(MovieEntity_.releaseDates)).thenReturn(this.releaseDateEntityJoin);
        final Predicate fromDateInPredicate = Mockito.mock(Predicate.class);
        Mockito
                .when(
                        this.cb.and(
                                this.cb.greaterThanOrEqualTo(fromDatePath, FROM_DATE),
                                this.cb.equal(releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                        )
                ).thenReturn(fromDateInPredicate);

        final Path<Date> toDatePath = (Path<Date>) Mockito.mock(Path.class);
        this.releaseDateEntityJoin = (ListJoin<MovieEntity, MovieReleaseDateEntity>) Mockito.mock(ListJoin.class);
        Mockito.when(movieReleaseDateEntityRoot.get(MovieReleaseDateEntity_.date)).thenReturn(toDatePath);
        Mockito.when(movieReleaseDateEntityRoot.get(MovieReleaseDateEntity_.status)).thenReturn(statusPath);
        Mockito.when(this.movieRoot.join(MovieEntity_.releaseDates)).thenReturn(this.releaseDateEntityJoin);
        final Predicate toDateInPredicate = Mockito.mock(Predicate.class);
        Mockito
                .when(
                        this.cb.and(
                                this.cb.lessThanOrEqualTo(toDatePath, TO_DATE),
                                this.cb.equal(releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                        )
                ).thenReturn(toDateInPredicate);

        final Path<CountryType> countryPath = (Path<CountryType>) Mockito.mock(Path.class);
        this.countryEntityJoin = (ListJoin<MovieEntity, MovieCountryEntity>) Mockito.mock(ListJoin.class);
        Mockito.when(movieCountryEntityRoot.get(MovieCountryEntity_.country)).thenReturn(countryPath);
        Mockito.when(movieCountryEntityRoot.get(MovieCountryEntity_.status)).thenReturn(statusPath);
        Mockito.when(this.movieRoot.join(MovieEntity_.countries)).thenReturn(this.countryEntityJoin);
        final Predicate countryInPredicate = Mockito.mock(Predicate.class);
        Mockito
                .when(
                        this.cb.and(
                                this.cb.equal(Mockito.eq(countryPath), Mockito.any(CountryType.class)),
                                this.cb.equal(countryEntityJoin.get(MovieCountryEntity_.status), DataStatus.ACCEPTED)
                        )
                ).thenReturn(countryInPredicate);

        final Path<LanguageType> languagePath = (Path<LanguageType>) Mockito.mock(Path.class);
        this.languageEntityJoin = (ListJoin<MovieEntity, MovieLanguageEntity>) Mockito.mock(ListJoin.class);
        Mockito.when(movieLanguageEntityRoot.get(MovieLanguageEntity_.language)).thenReturn(languagePath);
        Mockito.when(movieLanguageEntityRoot.get(MovieLanguageEntity_.status)).thenReturn(statusPath);
        Mockito.when(this.movieRoot.join(MovieEntity_.languages)).thenReturn(this.languageEntityJoin);
        final Predicate languageInPredicate = Mockito.mock(Predicate.class);
        Mockito
                .when(
                        this.cb.and(
                                this.cb.equal(Mockito.eq(languagePath), Mockito.any(LanguageType.class)),
                                this.cb.equal(languageEntityJoin.get(MovieCountryEntity_.status), DataStatus.ACCEPTED)
                        )
                ).thenReturn(languageInPredicate);

        final Path<GenreType> genrePath = (Path<GenreType>) Mockito.mock(Path.class);
        this.genreEntityJoin = (ListJoin<MovieEntity, MovieGenreEntity>) Mockito.mock(ListJoin.class);
        Mockito.when(movieGenreEntityRoot.get(MovieGenreEntity_.genre)).thenReturn(genrePath);
        Mockito.when(movieGenreEntityRoot.get(MovieGenreEntity_.status)).thenReturn(statusPath);
        Mockito.when(this.movieRoot.join(MovieEntity_.genres)).thenReturn(this.genreEntityJoin);
        final Predicate genreInPredicate = Mockito.mock(Predicate.class);
        Mockito
                .when(
                        this.cb.and(
                                this.cb.equal(Mockito.eq(genrePath), Mockito.any(GenreType.class)),
                                this.cb.equal(genreEntityJoin.get(MovieCountryEntity_.status), DataStatus.ACCEPTED)
                        )
                ).thenReturn(genreInPredicate);

        final Path<Float> minRatingPath = (Path<Float>) Mockito.mock(Path.class);
        final Predicate greaterThanOrEqualToMinRatingPredicate = Mockito.mock(Predicate.class);
        Mockito.when(this.movieRoot.get(MovieEntity_.rating)).thenReturn(minRatingPath);
        Mockito.when(this.cb.equal(equalTypePredicate, MIN_RATING)).thenReturn(greaterThanOrEqualToMinRatingPredicate);

        final Path<Float> maxRatingPath = (Path<Float>) Mockito.mock(Path.class);
        final Predicate lessThanOrEqualToMaxRatingPredicate = Mockito.mock(Predicate.class);
        Mockito.when(this.movieRoot.get(MovieEntity_.rating)).thenReturn(maxRatingPath);
        Mockito.when(this.cb.equal(maxRatingPath, MAX_RATING)).thenReturn(lessThanOrEqualToMaxRatingPredicate);
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindWithAll() {
        MovieSpecs.getFindPredicate(
                this.movieRoot,
                this.cb,
                TITLE,
                TYPE,
                FROM_DATE,
                TO_DATE,
                COUNTRIES,
                LANGUAGES,
                GENRES,
                MIN_RATING,
                MAX_RATING
        );

        Mockito.verify(this.movieRoot, Mockito.times(6)).join(MovieEntity_.otherTitles);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.like(
                                this.cb.upper(this.otherTitleEntityJoin.get(MovieOtherTitleEntity_.title)),
                                "%" + TITLE.toUpperCase() + "%"
                        ),
                        this.cb.equal(this.otherTitleEntityJoin.get(MovieOtherTitleEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.movieRoot.get(MovieEntity_.type), TYPE);
        Mockito.verify(this.movieRoot, Mockito.times(6)).join(MovieEntity_.releaseDates);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.greaterThanOrEqualTo(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.date), FROM_DATE),
                        this.cb.equal(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.lessThanOrEqualTo(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.date), TO_DATE),
                        this.cb.equal(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito.verify(this.movieRoot, Mockito.times(6)).join(MovieEntity_.countries);
        COUNTRIES.forEach(
                country ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.countryEntityJoin.get(MovieCountryEntity_.country), country),
                                        this.cb.equal(this.countryEntityJoin.get(MovieCountryEntity_.status), DataStatus.ACCEPTED)
                                )
        );
        Mockito.verify(this.movieRoot, Mockito.times(6)).join(MovieEntity_.languages);
        LANGUAGES.forEach(
                language ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.languageEntityJoin.get(MovieLanguageEntity_.language), language),
                                        this.cb.equal(this.languageEntityJoin.get(MovieLanguageEntity_.status), DataStatus.ACCEPTED)
                                )

        );
        Mockito.verify(this.movieRoot, Mockito.times(6)).join(MovieEntity_.genres);
        GENRES.forEach(
                genre ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.genreEntityJoin.get(MovieGenreEntity_.genre), genre),
                                        this.cb.equal(this.genreEntityJoin.get(MovieGenreEntity_.status), DataStatus.ACCEPTED)
                                )

        );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .greaterThanOrEqualTo(this.movieRoot.get(MovieEntity_.rating), (float) MIN_RATING);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .lessThanOrEqualTo(this.movieRoot.get(MovieEntity_.rating), (float) MAX_RATING);
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindWithOutTitle() {
        MovieSpecs.getFindPredicate(
                this.movieRoot,
                this.cb,
                null,
                TYPE,
                FROM_DATE,
                TO_DATE,
                COUNTRIES,
                LANGUAGES,
                GENRES,
                MIN_RATING,
                MAX_RATING
        );

        Mockito
                .verify(this.cb, Mockito.never())
                .and(
                        this.cb.like(
                                this.cb.upper(this.otherTitleEntityJoin.get(MovieOtherTitleEntity_.title)),
                                "%" + TITLE.toUpperCase() + "%"
                        ),
                        this.cb.equal(this.otherTitleEntityJoin.get(MovieOtherTitleEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.movieRoot.get(MovieEntity_.type), TYPE);
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.releaseDates);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.greaterThanOrEqualTo(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.date), FROM_DATE),
                        this.cb.equal(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.lessThanOrEqualTo(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.date), TO_DATE),
                        this.cb.equal(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.countries);
        COUNTRIES.forEach(
                country ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.countryEntityJoin.get(MovieCountryEntity_.country), country),
                                        this.cb.equal(this.countryEntityJoin.get(MovieCountryEntity_.status), DataStatus.ACCEPTED)
                                )
        );
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.languages);
        LANGUAGES.forEach(
                language ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.languageEntityJoin.get(MovieLanguageEntity_.language), language),
                                        this.cb.equal(this.languageEntityJoin.get(MovieLanguageEntity_.status), DataStatus.ACCEPTED)
                                )

        );
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.genres);
        GENRES.forEach(
                genre ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.genreEntityJoin.get(MovieGenreEntity_.genre), genre),
                                        this.cb.equal(this.genreEntityJoin.get(MovieGenreEntity_.status), DataStatus.ACCEPTED)
                                )

        );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .greaterThanOrEqualTo(this.movieRoot.get(MovieEntity_.rating), (float) MIN_RATING);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .lessThanOrEqualTo(this.movieRoot.get(MovieEntity_.rating), (float) MAX_RATING);
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindWithOutType() {
        MovieSpecs.getFindPredicate(
                this.movieRoot,
                this.cb,
                TITLE,
                null,
                FROM_DATE,
                TO_DATE,
                COUNTRIES,
                LANGUAGES,
                GENRES,
                MIN_RATING,
                MAX_RATING
        );

        Mockito.verify(this.movieRoot, Mockito.times(6)).join(MovieEntity_.otherTitles);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.like(
                                this.cb.upper(this.otherTitleEntityJoin.get(MovieOtherTitleEntity_.title)),
                                "%" + TITLE.toUpperCase() + "%"
                        ),
                        this.cb.equal(this.otherTitleEntityJoin.get(MovieOtherTitleEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito
                .verify(this.cb, Mockito.never())
                .equal(this.movieRoot.get(MovieEntity_.type), TYPE);
        Mockito.verify(this.movieRoot, Mockito.times(6)).join(MovieEntity_.releaseDates);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.greaterThanOrEqualTo(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.date), FROM_DATE),
                        this.cb.equal(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.lessThanOrEqualTo(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.date), TO_DATE),
                        this.cb.equal(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito.verify(this.movieRoot, Mockito.times(6)).join(MovieEntity_.countries);
        COUNTRIES.forEach(
                country ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.countryEntityJoin.get(MovieCountryEntity_.country), country),
                                        this.cb.equal(this.countryEntityJoin.get(MovieCountryEntity_.status), DataStatus.ACCEPTED)
                                )
        );
        Mockito.verify(this.movieRoot, Mockito.times(6)).join(MovieEntity_.languages);
        LANGUAGES.forEach(
                language ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.languageEntityJoin.get(MovieLanguageEntity_.language), language),
                                        this.cb.equal(this.languageEntityJoin.get(MovieLanguageEntity_.status), DataStatus.ACCEPTED)
                                )

        );
        Mockito.verify(this.movieRoot, Mockito.times(6)).join(MovieEntity_.genres);
        GENRES.forEach(
                genre ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.genreEntityJoin.get(MovieGenreEntity_.genre), genre),
                                        this.cb.equal(this.genreEntityJoin.get(MovieGenreEntity_.status), DataStatus.ACCEPTED)
                                )

        );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .greaterThanOrEqualTo(this.movieRoot.get(MovieEntity_.rating), (float) MIN_RATING);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .lessThanOrEqualTo(this.movieRoot.get(MovieEntity_.rating), (float) MAX_RATING);
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindWithOutFromDate() {
        MovieSpecs.getFindPredicate(
                this.movieRoot,
                this.cb,
                TITLE,
                TYPE,
                null,
                TO_DATE,
                COUNTRIES,
                LANGUAGES,
                GENRES,
                MIN_RATING,
                MAX_RATING
        );

        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.otherTitles);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.like(
                                this.cb.upper(this.otherTitleEntityJoin.get(MovieOtherTitleEntity_.title)),
                                "%" + TITLE.toUpperCase() + "%"
                        ),
                        this.cb.equal(this.otherTitleEntityJoin.get(MovieOtherTitleEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.movieRoot.get(MovieEntity_.type), TYPE);
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.releaseDates);
        Mockito
                .verify(this.cb, Mockito.never())
                .and(
                        this.cb.greaterThanOrEqualTo(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.date), FROM_DATE),
                        this.cb.equal(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.lessThanOrEqualTo(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.date), TO_DATE),
                        this.cb.equal(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.countries);
        COUNTRIES.forEach(
                country ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.countryEntityJoin.get(MovieCountryEntity_.country), country),
                                        this.cb.equal(this.countryEntityJoin.get(MovieCountryEntity_.status), DataStatus.ACCEPTED)
                                )
        );
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.languages);
        LANGUAGES.forEach(
                language ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.languageEntityJoin.get(MovieLanguageEntity_.language), language),
                                        this.cb.equal(this.languageEntityJoin.get(MovieLanguageEntity_.status), DataStatus.ACCEPTED)
                                )

        );
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.genres);
        GENRES.forEach(
                genre ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.genreEntityJoin.get(MovieGenreEntity_.genre), genre),
                                        this.cb.equal(this.genreEntityJoin.get(MovieGenreEntity_.status), DataStatus.ACCEPTED)
                                )

        );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .greaterThanOrEqualTo(this.movieRoot.get(MovieEntity_.rating), (float) MIN_RATING);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .lessThanOrEqualTo(this.movieRoot.get(MovieEntity_.rating), (float) MAX_RATING);
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindWithOutToDate() {
        MovieSpecs.getFindPredicate(
                this.movieRoot,
                this.cb,
                TITLE,
                TYPE,
                TO_DATE,
                null,
                COUNTRIES,
                LANGUAGES,
                GENRES,
                MIN_RATING,
                MAX_RATING
        );

        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.otherTitles);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.like(
                                this.cb.upper(this.otherTitleEntityJoin.get(MovieOtherTitleEntity_.title)),
                                "%" + TITLE.toUpperCase() + "%"
                        ),
                        this.cb.equal(this.otherTitleEntityJoin.get(MovieOtherTitleEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.movieRoot.get(MovieEntity_.type), TYPE);
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.releaseDates);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.greaterThanOrEqualTo(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.date), FROM_DATE),
                        this.cb.equal(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito
                .verify(this.cb, Mockito.never())
                .and(
                        this.cb.lessThanOrEqualTo(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.date), TO_DATE),
                        this.cb.equal(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.countries);
        COUNTRIES.forEach(
                country ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.countryEntityJoin.get(MovieCountryEntity_.country), country),
                                        this.cb.equal(this.countryEntityJoin.get(MovieCountryEntity_.status), DataStatus.ACCEPTED)
                                )
        );
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.languages);
        LANGUAGES.forEach(
                language ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.languageEntityJoin.get(MovieLanguageEntity_.language), language),
                                        this.cb.equal(this.languageEntityJoin.get(MovieLanguageEntity_.status), DataStatus.ACCEPTED)
                                )

        );
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.genres);
        GENRES.forEach(
                genre ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.genreEntityJoin.get(MovieGenreEntity_.genre), genre),
                                        this.cb.equal(this.genreEntityJoin.get(MovieGenreEntity_.status), DataStatus.ACCEPTED)
                                )

        );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .greaterThanOrEqualTo(this.movieRoot.get(MovieEntity_.rating), (float) MIN_RATING);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .lessThanOrEqualTo(this.movieRoot.get(MovieEntity_.rating), (float) MAX_RATING);
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindWithOutCountries() {
        MovieSpecs.getFindPredicate(
                this.movieRoot,
                this.cb,
                TITLE,
                TYPE,
                FROM_DATE,
                TO_DATE,
                null,
                LANGUAGES,
                GENRES,
                MIN_RATING,
                MAX_RATING
        );

        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.otherTitles);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.like(
                                this.cb.upper(this.otherTitleEntityJoin.get(MovieOtherTitleEntity_.title)),
                                "%" + TITLE.toUpperCase() + "%"
                        ),
                        this.cb.equal(this.otherTitleEntityJoin.get(MovieOtherTitleEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.movieRoot.get(MovieEntity_.type), TYPE);
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.releaseDates);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.greaterThanOrEqualTo(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.date), FROM_DATE),
                        this.cb.equal(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.lessThanOrEqualTo(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.date), TO_DATE),
                        this.cb.equal(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.countries);
        COUNTRIES.forEach(
                country ->
                        Mockito
                                .verify(this.cb, Mockito.never())
                                .and(
                                        this.cb.equal(this.countryEntityJoin.get(MovieCountryEntity_.country), country),
                                        this.cb.equal(this.countryEntityJoin.get(MovieCountryEntity_.status), DataStatus.ACCEPTED)
                                )
        );
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.languages);
        LANGUAGES.forEach(
                language ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.languageEntityJoin.get(MovieLanguageEntity_.language), language),
                                        this.cb.equal(this.languageEntityJoin.get(MovieLanguageEntity_.status), DataStatus.ACCEPTED)
                                )

        );
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.genres);
        GENRES.forEach(
                genre ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.genreEntityJoin.get(MovieGenreEntity_.genre), genre),
                                        this.cb.equal(this.genreEntityJoin.get(MovieGenreEntity_.status), DataStatus.ACCEPTED)
                                )

        );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .greaterThanOrEqualTo(this.movieRoot.get(MovieEntity_.rating), (float) MIN_RATING);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .lessThanOrEqualTo(this.movieRoot.get(MovieEntity_.rating), (float) MAX_RATING);
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindWithOutLanguages() {
        MovieSpecs.getFindPredicate(
                this.movieRoot,
                this.cb,
                TITLE,
                TYPE,
                FROM_DATE,
                TO_DATE,
                COUNTRIES,
                null,
                GENRES,
                MIN_RATING,
                MAX_RATING
        );

        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.otherTitles);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.like(
                                this.cb.upper(this.otherTitleEntityJoin.get(MovieOtherTitleEntity_.title)),
                                "%" + TITLE.toUpperCase() + "%"
                        ),
                        this.cb.equal(this.otherTitleEntityJoin.get(MovieOtherTitleEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.movieRoot.get(MovieEntity_.type), TYPE);
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.releaseDates);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.greaterThanOrEqualTo(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.date), FROM_DATE),
                        this.cb.equal(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.lessThanOrEqualTo(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.date), TO_DATE),
                        this.cb.equal(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.countries);
        COUNTRIES.forEach(
                country ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.countryEntityJoin.get(MovieCountryEntity_.country), country),
                                        this.cb.equal(this.countryEntityJoin.get(MovieCountryEntity_.status), DataStatus.ACCEPTED)
                                )
        );
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.languages);
        LANGUAGES.forEach(
                language ->
                        Mockito
                                .verify(this.cb, Mockito.never())
                                .and(
                                        this.cb.equal(this.languageEntityJoin.get(MovieLanguageEntity_.language), language),
                                        this.cb.equal(this.languageEntityJoin.get(MovieLanguageEntity_.status), DataStatus.ACCEPTED)
                                )

        );
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.genres);
        GENRES.forEach(
                genre ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.genreEntityJoin.get(MovieGenreEntity_.genre), genre),
                                        this.cb.equal(this.genreEntityJoin.get(MovieGenreEntity_.status), DataStatus.ACCEPTED)
                                )

        );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .greaterThanOrEqualTo(this.movieRoot.get(MovieEntity_.rating), (float) MIN_RATING);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .lessThanOrEqualTo(this.movieRoot.get(MovieEntity_.rating), (float) MAX_RATING);
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindWithOutGenres() {
        MovieSpecs.getFindPredicate(
                this.movieRoot,
                this.cb,
                TITLE,
                TYPE,
                FROM_DATE,
                TO_DATE,
                COUNTRIES,
                LANGUAGES,
                null,
                MIN_RATING,
                MAX_RATING
        );

        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.otherTitles);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.like(
                                this.cb.upper(this.otherTitleEntityJoin.get(MovieOtherTitleEntity_.title)),
                                "%" + TITLE.toUpperCase() + "%"
                        ),
                        this.cb.equal(this.otherTitleEntityJoin.get(MovieOtherTitleEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.movieRoot.get(MovieEntity_.type), TYPE);
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.releaseDates);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.greaterThanOrEqualTo(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.date), FROM_DATE),
                        this.cb.equal(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.lessThanOrEqualTo(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.date), TO_DATE),
                        this.cb.equal(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.countries);
        COUNTRIES.forEach(
                country ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.countryEntityJoin.get(MovieCountryEntity_.country), country),
                                        this.cb.equal(this.countryEntityJoin.get(MovieCountryEntity_.status), DataStatus.ACCEPTED)
                                )
        );
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.languages);
        LANGUAGES.forEach(
                language ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.languageEntityJoin.get(MovieLanguageEntity_.language), language),
                                        this.cb.equal(this.languageEntityJoin.get(MovieLanguageEntity_.status), DataStatus.ACCEPTED)
                                )

        );
        Mockito.verify(this.movieRoot, Mockito.times(5)).join(MovieEntity_.genres);
        GENRES.forEach(
                genre ->
                        Mockito
                                .verify(this.cb, Mockito.never())
                                .and(
                                        this.cb.equal(this.genreEntityJoin.get(MovieGenreEntity_.genre), genre),
                                        this.cb.equal(this.genreEntityJoin.get(MovieGenreEntity_.status), DataStatus.ACCEPTED)
                                )

        );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .greaterThanOrEqualTo(this.movieRoot.get(MovieEntity_.rating), (float) MIN_RATING);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .lessThanOrEqualTo(this.movieRoot.get(MovieEntity_.rating), (float) MAX_RATING);
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindWithOutMinRating() {
        MovieSpecs.getFindPredicate(
                this.movieRoot,
                this.cb,
                TITLE,
                TYPE,
                FROM_DATE,
                TO_DATE,
                COUNTRIES,
                LANGUAGES,
                GENRES,
                null,
                MAX_RATING
        );

        Mockito.verify(this.movieRoot, Mockito.times(6)).join(MovieEntity_.otherTitles);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.like(
                                this.cb.upper(this.otherTitleEntityJoin.get(MovieOtherTitleEntity_.title)),
                                "%" + TITLE.toUpperCase() + "%"
                        ),
                        this.cb.equal(this.otherTitleEntityJoin.get(MovieOtherTitleEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.movieRoot.get(MovieEntity_.type), TYPE);
        Mockito.verify(this.movieRoot, Mockito.times(6)).join(MovieEntity_.releaseDates);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.greaterThanOrEqualTo(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.date), FROM_DATE),
                        this.cb.equal(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.lessThanOrEqualTo(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.date), TO_DATE),
                        this.cb.equal(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito.verify(this.movieRoot, Mockito.times(6)).join(MovieEntity_.countries);
        COUNTRIES.forEach(
                country ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.countryEntityJoin.get(MovieCountryEntity_.country), country),
                                        this.cb.equal(this.countryEntityJoin.get(MovieCountryEntity_.status), DataStatus.ACCEPTED)
                                )
        );
        Mockito.verify(this.movieRoot, Mockito.times(6)).join(MovieEntity_.languages);
        LANGUAGES.forEach(
                language ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.languageEntityJoin.get(MovieLanguageEntity_.language), language),
                                        this.cb.equal(this.languageEntityJoin.get(MovieLanguageEntity_.status), DataStatus.ACCEPTED)
                                )

        );
        Mockito.verify(this.movieRoot, Mockito.times(6)).join(MovieEntity_.genres);
        GENRES.forEach(
                genre ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.genreEntityJoin.get(MovieGenreEntity_.genre), genre),
                                        this.cb.equal(this.genreEntityJoin.get(MovieGenreEntity_.status), DataStatus.ACCEPTED)
                                )

        );
        Mockito
                .verify(this.cb, Mockito.never())
                .greaterThanOrEqualTo(this.movieRoot.get(MovieEntity_.rating), (float) MIN_RATING);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .lessThanOrEqualTo(this.movieRoot.get(MovieEntity_.rating), (float) MAX_RATING);
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindWithOutMaxRating() {
        MovieSpecs.getFindPredicate(
                this.movieRoot,
                this.cb,
                TITLE,
                TYPE,
                FROM_DATE,
                TO_DATE,
                COUNTRIES,
                LANGUAGES,
                GENRES,
                MIN_RATING,
                null
        );

        Mockito.verify(this.movieRoot, Mockito.times(6)).join(MovieEntity_.otherTitles);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.like(
                                this.cb.upper(this.otherTitleEntityJoin.get(MovieOtherTitleEntity_.title)),
                                "%" + TITLE.toUpperCase() + "%"
                        ),
                        this.cb.equal(this.otherTitleEntityJoin.get(MovieOtherTitleEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.movieRoot.get(MovieEntity_.type), TYPE);
        Mockito.verify(this.movieRoot, Mockito.times(6)).join(MovieEntity_.releaseDates);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.greaterThanOrEqualTo(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.date), FROM_DATE),
                        this.cb.equal(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .and(
                        this.cb.lessThanOrEqualTo(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.date), TO_DATE),
                        this.cb.equal(this.releaseDateEntityJoin.get(MovieReleaseDateEntity_.status), DataStatus.ACCEPTED)
                );
        Mockito.verify(this.movieRoot, Mockito.times(6)).join(MovieEntity_.countries);
        COUNTRIES.forEach(
                country ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.countryEntityJoin.get(MovieCountryEntity_.country), country),
                                        this.cb.equal(this.countryEntityJoin.get(MovieCountryEntity_.status), DataStatus.ACCEPTED)
                                )
        );
        Mockito.verify(this.movieRoot, Mockito.times(6)).join(MovieEntity_.languages);
        LANGUAGES.forEach(
                language ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.languageEntityJoin.get(MovieLanguageEntity_.language), language),
                                        this.cb.equal(this.languageEntityJoin.get(MovieLanguageEntity_.status), DataStatus.ACCEPTED)
                                )

        );
        Mockito.verify(this.movieRoot, Mockito.times(6)).join(MovieEntity_.genres);
        GENRES.forEach(
                genre ->
                        Mockito
                                .verify(this.cb, Mockito.times(1))
                                .and(
                                        this.cb.equal(this.genreEntityJoin.get(MovieGenreEntity_.genre), genre),
                                        this.cb.equal(this.genreEntityJoin.get(MovieGenreEntity_.status), DataStatus.ACCEPTED)
                                )

        );
        Mockito
                .verify(this.cb, Mockito.times(1))
                .greaterThanOrEqualTo(this.movieRoot.get(MovieEntity_.rating), (float) MIN_RATING);
        Mockito
                .verify(this.cb, Mockito.never())
                .lessThanOrEqualTo(this.movieRoot.get(MovieEntity_.rating), (float) MAX_RATING);
    }
}
