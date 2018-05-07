package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.MovieField;
import lombok.*;

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
}
