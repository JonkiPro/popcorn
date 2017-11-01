package com.core.jpa.entity.movie;

import com.core.movie.EditStatus;
import com.core.jpa.entity.MovieEntity;
import com.core.jpa.entity.UserEntity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Representation of the movie's metadata info.
 */
@Generated(value = "org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor")
@StaticMetamodel(MovieInfo.class)
public abstract class MovieInfo_ {
    public static volatile SingularAttribute<MovieInfo, Long> id;
    public static volatile SingularAttribute<MovieInfo, MovieEntity> movie;
    public static volatile SingularAttribute<MovieInfo, UserEntity> user;
    public static volatile SingularAttribute<MovieInfo, EditStatus> status;
}
