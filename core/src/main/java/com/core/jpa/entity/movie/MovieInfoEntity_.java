package com.core.jpa.entity.movie;

import com.core.movie.DataStatus;
import com.core.jpa.entity.MovieEntity;
import com.core.jpa.entity.UserEntity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Representation of the movie's metadata info.
 */
@Generated(value = "org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor")
@StaticMetamodel(MovieInfoEntity.class)
public abstract class MovieInfoEntity_ {
    public static volatile SingularAttribute<MovieInfoEntity, Long> id;
    public static volatile SingularAttribute<MovieInfoEntity, MovieEntity> movie;
    public static volatile SingularAttribute<MovieInfoEntity, UserEntity> user;
    public static volatile SingularAttribute<MovieInfoEntity, DataStatus> status;
}