package com.core.jpa.entity.movie;

import com.common.dto.movie.CountryType;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Representation of the movie's metamodel country.
 */
@Generated(value = "org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor")
@StaticMetamodel(MovieCountry.class)
public abstract class MovieCountry_ extends MovieInfo_ {
    public static volatile SingularAttribute<MovieCountry, CountryType> country;
}
