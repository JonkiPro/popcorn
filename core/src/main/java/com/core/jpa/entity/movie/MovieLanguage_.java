package com.core.jpa.entity.movie;

import com.common.dto.movie.LanguageType;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Representation of the movie's metamodel language.
 */
@Generated(value = "org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor")
@StaticMetamodel(MovieLanguage.class)
public class MovieLanguage_ extends MovieInfo_ {
    public static volatile SingularAttribute<MovieLanguage, LanguageType> language;
}
