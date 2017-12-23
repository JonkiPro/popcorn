package com.core.jpa.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

/**
 * Representation of the audit's metamodel.
 */
@Generated(value = "org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor")
@StaticMetamodel(AuditEntity.class)
public abstract class AuditEntity_ extends IdEntity_ {
    public static volatile SingularAttribute<AuditEntity, Date> created;
    public static volatile SingularAttribute<AuditEntity, Integer> entityVersion;
}
