package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.TitleAttribute;
import com.jonki.popcorn.common.dto.movie.type.CountryType;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Representation of the movie's metamodel other title.
 */
@Generated(value = "org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor")
@StaticMetamodel(MovieOtherTitleEntity.class)
public abstract class MovieOtherTitleEntity_ extends MovieInfoEntity_ {
    public static volatile SingularAttribute<MovieOtherTitleEntity, String> title;
    public static volatile SingularAttribute<MovieOtherTitleEntity, CountryType> country;
    public static volatile SingularAttribute<MovieOtherTitleEntity, TitleAttribute> attribute;
}
