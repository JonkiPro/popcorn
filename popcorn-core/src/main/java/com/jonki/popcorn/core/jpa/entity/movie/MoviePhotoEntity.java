package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.MovieField;
import com.jonki.popcorn.common.dto.StorageProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Representation of photo.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DiscriminatorValue(value = MovieField.Values.PHOTO)
public class MoviePhotoEntity extends MovieFileEntity {

    private static final long serialVersionUID = -3600516702030372067L;

    /**
     * Constructor with all variables.
     *
     * @param idInCloud File ID in the cloud
     * @param provider Cloud provider
     */
    public MoviePhotoEntity(final String idInCloud, final StorageProvider provider) {
        super(idInCloud, provider);
    }
}
