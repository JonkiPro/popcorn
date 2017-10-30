package com.core.jpa.entity.movie;

import com.common.dto.movie.GenreType;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Representation of the movie's metamodel genre.
 */
@Generated(value = "org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor")
@StaticMetamodel(MovieGenre.class)
public class MovieGenre_ extends MovieInfo_ {
    public static volatile SingularAttribute<MovieGenre, GenreType> genre;
}
