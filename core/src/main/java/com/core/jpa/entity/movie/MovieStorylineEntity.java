package com.core.jpa.entity.movie;

import com.common.dto.movie.Storyline;
import com.core.movie.MovieField;
import lombok.*;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Representation of the movie storyline.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue(value = MovieField.Values.STORYLINE)
public class MovieStorylineEntity extends MovieInfoEntity {

    private static final long serialVersionUID = 1148763870924794823L;

    @Basic
    private String storyline;


    /**
     * Get a DTO representing this site.
     *
     * @return The read-only DTO.
     */
    public Storyline getDTO() {
        return Storyline.builder()
                .storyline(this.storyline)
                .build();
    }
}
