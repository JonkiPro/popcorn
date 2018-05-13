package com.jonki.popcorn.core.jpa.entity;

import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.MovieField;

import javax.annotation.Generated;
import javax.persistence.metamodel.MapAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

/**
 * Representation of the contribution's metamodel.
 */
@Generated(value = "org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor")
@StaticMetamodel(ContributionEntity.class)
public abstract class ContributionEntity_ {
    public static volatile SingularAttribute<ContributionEntity, Long> id;
    public static volatile SingularAttribute<ContributionEntity, MovieEntity> movie;
    public static volatile SingularAttribute<ContributionEntity, UserEntity> user;
    public static volatile SetAttribute<ContributionEntity, Long> idsToAdd;
    public static volatile MapAttribute<ContributionEntity, Long, Long> idsToUpdate;
    public static volatile SetAttribute<ContributionEntity, Long> idsToDelete;
    public static volatile SingularAttribute<ContributionEntity, DataStatus> status;
    public static volatile SingularAttribute<ContributionEntity, MovieField> field;
    public static volatile SetAttribute<ContributionEntity, String> sources;
    public static volatile SingularAttribute<ContributionEntity, String> userComment;
    public static volatile SingularAttribute<ContributionEntity, Date> created;
    public static volatile SingularAttribute<ContributionEntity, Date> verificationDate;
    public static volatile SingularAttribute<ContributionEntity, UserEntity> verificationUser;
    public static volatile SingularAttribute<ContributionEntity, String> verificationComment;
    public static volatile SingularAttribute<ContributionEntity, Integer> version;
}
