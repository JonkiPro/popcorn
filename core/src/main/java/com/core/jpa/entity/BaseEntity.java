package com.core.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * Abstract class to support basic column UUID.
 */
@MappedSuperclass
@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 4076477009276822012L;

    @Column(updatable = false, nullable = false, unique = true)
    private String uuid;

    /**
     * Generate a UUID if it is null.
     */
    @PrePersist
    protected void onCreateBaseEntity() {
        // Make sure we have an uuid if one wasn't entered beforehand
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID().toString();
        }
    }
}
