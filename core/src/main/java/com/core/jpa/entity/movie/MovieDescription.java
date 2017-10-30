package com.core.jpa.entity.movie;

import com.core.movie.MovieField;
import lombok.*;

import javax.persistence.*;

/**
 * Representation of the movie description.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@DiscriminatorValue(value = MovieField.Values.DESCRIPTION)
public class MovieDescription extends MovieInfo {

    private String description;
}
