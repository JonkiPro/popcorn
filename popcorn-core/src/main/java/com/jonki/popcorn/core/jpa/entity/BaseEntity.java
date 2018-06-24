package com.jonki.popcorn.core.jpa.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * The base for entities. e.g. Users and Messages.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = {"uniqueId"})
@ToString(callSuper = true, of = {"uniqueId"})
@MappedSuperclass
public class BaseEntity extends AuditEntity {

    private static final long serialVersionUID = 4076477009276822012L;

    @Basic(optional = false)
    @Column(name = "unique_id", nullable = false, unique = true, updatable = false)
    @NotBlank
    @Size(max = 255)
    private String uniqueId = UUID.randomUUID().toString();

    /**
     * Default constructor.
     */
    BaseEntity() {
        super();
    }

    /**
     * Generate a uniqueId if it is null.
     */
    @PrePersist
    protected void onCreateBaseEntity() {
        // Make sure we have an uuid if one wasn't entered beforehand
        if (this.uniqueId == null) {
            this.uniqueId = UUID.randomUUID().toString();
        }
    }
}
