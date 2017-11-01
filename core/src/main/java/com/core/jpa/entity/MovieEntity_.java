package com.core.jpa.entity;

import com.core.movie.EditStatus;
import com.common.dto.movie.MovieType;
import com.core.jpa.entity.movie.*;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
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
    public static volatile SetAttribute<MovieEntity, String> descriptions;
    public static volatile SetAttribute<MovieEntity, MovieOtherTitle> otherTitles;
    public static volatile SetAttribute<MovieEntity, MovieReview> reviews;
    public static volatile SetAttribute<MovieEntity, MovieStoryline> storylines;
    public static volatile SetAttribute<MovieEntity, MovieBoxOffice> boxOffices;
    public static volatile SetAttribute<MovieEntity, MovieReleaseDate> releaseDates;
    public static volatile SetAttribute<MovieEntity, MovieSite> sites;
    public static volatile SetAttribute<MovieEntity, MovieCountry> countries;
    public static volatile SetAttribute<MovieEntity, MovieLanguage> languages;
    public static volatile SetAttribute<MovieEntity, MovieGenre> genres;
    public static volatile SetAttribute<MovieEntity, MovieRate> ratings;
    public static volatile SingularAttribute<MovieEntity, String> budget;
    public static volatile SingularAttribute<MovieEntity, EditStatus> status;
    public static volatile SingularAttribute<MovieEntity, Float> rating;
}
