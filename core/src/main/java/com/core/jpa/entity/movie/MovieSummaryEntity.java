package com.core.jpa.entity.movie;

import com.common.dto.MovieField;
import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

/**
 * Representation of the movie summary.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DiscriminatorValue(value = MovieField.Values.SUMMARY)
public class MovieSummaryEntity extends MovieInfoEntity {

    private static final long serialVersionUID = 2550201360628804372L;

    @Basic
    @Column(name = "summary", length = 2000)
    @Size(min = 239, max = 2000)
    private String summary;
}
