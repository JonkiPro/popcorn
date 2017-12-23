package com.core.jpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Abstract class to support basic columns for entities. e.g. Users and Messages.
 */
@Getter
@ToString(callSuper = true, exclude = "entityVersion")
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class AuditEntity extends IdEntity {

    private static final long serialVersionUID = 612497590705223660L;

    @Basic(optional = false)
    @Column(name = "created", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date created;

    @Version
    @Column(name = "entity_version", nullable = false)
    @Getter(AccessLevel.NONE)
    private Integer entityVersion;
}
