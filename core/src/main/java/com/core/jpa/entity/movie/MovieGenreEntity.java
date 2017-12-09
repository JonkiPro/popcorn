package com.core.jpa.entity.movie;

import com.common.dto.movie.type.GenreType;
import com.common.dto.movie.Genre;
import com.core.movie.MovieField;
import lombok.*;

import javax.persistence.*;

/**
 * Representation of the movie genre.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue(value = MovieField.Values.GENRE)
public class MovieGenreEntity extends MovieInfoEntity {

    private static final long serialVersionUID = 5848617668106162823L;

    @Basic
    @Enumerated(EnumType.STRING)
    private GenreType genre;


    /**
     * Get a DTO representing this box office.
     *
     * @return The read-only DTO.
     */
    public Genre getDTO() {
        return Genre.builder()
                .genre(this.genre)
                .build();
    }
}
