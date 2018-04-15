package com.core.jpa.entity.movie;

import com.common.dto.MovieField;
import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

/**
 * Representation of the movie synopsis.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DiscriminatorValue(value = MovieField.Values.SYNOPSIS)
public class MovieSynopsisEntity extends MovieInfoEntity {

    private static final long serialVersionUID = 4427839880229899477L;

    @Basic
    @Column(name = "synopsis", length = 100000)
    @Size(max = 100000)
    private String synopsis;
}
