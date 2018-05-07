package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.movie.type.CountryType;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

/**
 * Representation of the movie's metamodel release date.
 */
@Generated(value = "org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor")
@StaticMetamodel(MovieReleaseDateEntity.class)
public abstract class MovieReleaseDateEntity_ extends MovieInfoEntity_ {
    public static volatile SingularAttribute<MovieReleaseDateEntity, Date> date;
    public static volatile SingularAttribute<MovieReleaseDateEntity, CountryType> country;
}
