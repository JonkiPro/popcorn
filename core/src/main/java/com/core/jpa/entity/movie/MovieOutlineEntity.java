package com.core.jpa.entity.movie;

import com.common.dto.MovieField;
import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

/**
 * Representation of the movie outline.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DiscriminatorValue(value = MovieField.Values.OUTLINE)
public class MovieOutlineEntity extends MovieInfoEntity {

    private static final long serialVersionUID = 4986796941529446588L;

    @Basic
    @Column(name = "outline", length = 239)
    @Size(max = 239)
    private String outline;
}
