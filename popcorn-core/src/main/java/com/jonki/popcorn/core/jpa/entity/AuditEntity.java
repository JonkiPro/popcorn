package com.jonki.popcorn.core.jpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import java.util.Date;

/**
 * Abstract class to support basic columns for entities. e.g. Users and Messages.
 */
@Getter
@ToString(callSuper = true, of = {"created"})
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class AuditEntity extends IdEntity {

    private static final long serialVersionUID = 612497590705223660L;

    @Basic(optional = false)
    @Column(name = "created", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date created = new Date();

    @Version
    @Column(name = "entity_version", nullable = false)
    @Getter(AccessLevel.NONE)
    private Integer entityVersion;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
