package com.core.jpa.entity.movie;

import com.common.dto.movie.GenreType;
import com.core.movie.MovieField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Representation of the movie genre.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@DiscriminatorValue(value = MovieField.Values.GENRE)
public class MovieGenre extends MovieInfo {

    @Enumerated(EnumType.STRING)
    private GenreType genre;
}
