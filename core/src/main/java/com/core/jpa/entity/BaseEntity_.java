package com.core.jpa.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Representation of the base's metamodel.
 */
@Generated(value = "org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor")
@StaticMetamodel(BaseEntity.class)
public abstract class BaseEntity_ extends AuditEntity_ {
    public static volatile SingularAttribute<BaseEntity, String> uniqueId;
}
