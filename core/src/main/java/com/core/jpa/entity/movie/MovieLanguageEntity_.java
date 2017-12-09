package com.core.jpa.entity.movie;

import com.common.dto.movie.type.LanguageType;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Representation of the movie's metamodel language.
 */
@Generated(value = "org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor")
@StaticMetamodel(MovieLanguageEntity.class)
public abstract class MovieLanguageEntity_ extends MovieInfoEntity_ {
    public static volatile SingularAttribute<MovieLanguageEntity, LanguageType> language;
}
