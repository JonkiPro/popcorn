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
 * Representation of poster.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DiscriminatorValue(value = MovieField.Values.POSTER)
public class MoviePosterEntity extends MovieFileEntity {

    private static final long serialVersionUID = -8422551973729439318L;

    /**
     * Constructor with all variables.
     *
     * @param idInCloud File ID in the cloud
     * @param provider Cloud provider
     */
    public MoviePosterEntity(final String idInCloud, final StorageProvider provider) {
        super(idInCloud, provider);
    }
}
