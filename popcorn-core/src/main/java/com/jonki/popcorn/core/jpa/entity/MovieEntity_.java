package com.jonki.popcorn.core.jpa.entity;

import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.movie.type.MovieType;
import com.jonki.popcorn.core.jpa.entity.movie.MovieBoxOfficeEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieCountryEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieGenreEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieLanguageEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieOtherTitleEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieOutlineEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieRateEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieReleaseDateEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieReviewEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieSiteEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieSummaryEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieSynopsisEntity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Representation of the movie's metamodel.
 */
@Generated(value = "org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor")
@StaticMetamodel(MovieEntity.class)
public abstract class MovieEntity_ {
    public static volatile SingularAttribute<MovieEntity, Long> id;
    public static volatile SingularAttribute<MovieEntity, String> title;
    public static volatile SingularAttribute<MovieEntity, MovieType> type;
    public static volatile ListAttribute<MovieEntity, MovieOtherTitleEntity> otherTitles;
    public static volatile ListAttribute<MovieEntity, MovieReleaseDateEntity> releaseDates;
    public static volatile ListAttribute<MovieEntity, MovieOutlineEntity> outlines;
    public static volatile ListAttribute<MovieEntity, MovieSummaryEntity> summaries;
    public static volatile ListAttribute<MovieEntity, MovieSynopsisEntity> synopses;
    public static volatile ListAttribute<MovieEntity, MovieBoxOfficeEntity> boxOffices;
    public static volatile ListAttribute<MovieEntity, MovieSiteEntity> sites;
    public static volatile ListAttribute<MovieEntity, MovieCountryEntity> countries;
    public static volatile ListAttribute<MovieEntity, MovieLanguageEntity> languages;
    public static volatile ListAttribute<MovieEntity, MovieGenreEntity> genres;
    public static volatile ListAttribute<MovieEntity, MovieReviewEntity> reviews;
    public static volatile ListAttribute<MovieEntity, MovieRateEntity> ratings;
    public static volatile SingularAttribute<MovieEntity, String> budget;
    public static volatile SingularAttribute<MovieEntity, DataStatus> status;
    public static volatile SingularAttribute<MovieEntity, Float> rating;
    public static volatile ListAttribute<MovieEntity, ContributionEntity> contributions;
}
