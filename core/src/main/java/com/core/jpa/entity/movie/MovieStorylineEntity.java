package com.core.jpa.entity.movie;

import com.common.dto.MovieField;
import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Representation of the movie storyline.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DiscriminatorValue(value = MovieField.Values.STORYLINE)
public class MovieStorylineEntity extends MovieInfoEntity {

    private static final long serialVersionUID = 1148763870924794823L;

    @Basic
    @Column(name = "storyline")
    private String storyline;
}
