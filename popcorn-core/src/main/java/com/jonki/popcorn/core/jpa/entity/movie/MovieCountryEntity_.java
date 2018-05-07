package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.movie.type.CountryType;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Representation of the movie's metamodel country.
 */
@Generated(value = "org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor")
@StaticMetamodel(MovieCountryEntity.class)
public abstract class MovieCountryEntity_ extends MovieInfoEntity_ {
    public static volatile SingularAttribute<MovieCountryEntity, CountryType> country;
}
