package com.core.jpa.entity.movie;

import com.common.dto.movie.CountryType;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

/**
 * Representation of the movie's metamodel release date.
 */
@Generated(value = "org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor")
@StaticMetamodel(MovieReleaseDate.class)
public class MovieReleaseDate_ {
    public static volatile SingularAttribute<MovieReleaseDate, Date> date;
    public static volatile SingularAttribute<MovieReleaseDate, CountryType> country;
}
