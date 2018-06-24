package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.StorageProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

/**
 * The base class for entities representing files.
 */
@NoArgsConstructor
@AllArgsConstructor
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
            this.provider = StorageProvider.GOOGLE;
        }
    }
}
