package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.MovieField;
import lombok.*;

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
}
