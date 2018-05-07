package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.movie.type.GenreType;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Representation of the movie's metamodel genre.
 */
@Generated(value = "org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor")
@StaticMetamodel(MovieGenreEntity.class)
public abstract class MovieGenreEntity_ extends MovieInfoEntity_ {
    public static volatile SingularAttribute<MovieGenreEntity, GenreType> genre;
}
