package com.core.jpa.entity.movie;

import com.common.dto.StorageProvider;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * The base class for entities representing files.
 */
@Getter
@Setter
@ToString(callSuper = true)
@MappedSuperclass
public class MovieFileEntity extends MovieInfoEntity {

    private static final long serialVersionUID = -4083228193753194958L;

    @Basic
    @Column(name = "file_id_in_cloud")
    private String idInCloud;

    @Basic
    @Column(name = "file_provider")
    @Enumerated(EnumType.STRING)
    private StorageProvider provider;

    /**
     * Assign the provider "GOOGLE" when saving a object and the provider is null.
     */
    @PrePersist
    protected void onCreateMovieFileEntity() {
        if(this.provider == null) {
            provider = StorageProvider.GOOGLE;
        }
    }
}
