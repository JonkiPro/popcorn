package com.core.jpa.entity;

import com.common.dto.SecurityRole;
import com.common.dto.UserMoviePermission;
import com.core.jpa.entity.movie.MovieRateEntity;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

/**
 * Representation of the user's metamodel.
 */
@Generated(value = "org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor")
@StaticMetamodel(UserEntity.class)
public abstract class UserEntity_ extends BaseEntity_ {
    public static volatile SingularAttribute<UserEntity, String> username;
    public static volatile SingularAttribute<UserEntity, String> email;
    public static volatile SingularAttribute<UserEntity, String> password;
    public static volatile SingularAttribute<UserEntity, String> activationToken;
    public static volatile SingularAttribute<UserEntity, String> emailChangeToken;
    public static volatile SingularAttribute<UserEntity, String> newEmail;
    public static volatile SingularAttribute<UserEntity, Boolean> enabled;
    public static volatile SetAttribute<UserEntity, SecurityRole> authorities;
    public static volatile ListAttribute<UserEntity, MessageEntity> sentMessages;
    public static volatile ListAttribute<UserEntity, MessageEntity> receivedMessages;
    public static volatile SetAttribute<UserEntity, UserMoviePermission> permissions;
    public static volatile SetAttribute<UserEntity, MovieRateEntity> ratings;
    public static volatile SetAttribute<UserEntity, ContributionEntity> contributions;
    public static volatile SingularAttribute<UserEntity, Date> modifiedDate;
}
