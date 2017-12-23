package com.core.jpa.entity.movie;

import com.common.dto.movie.type.GenreType;
import com.common.dto.MovieField;
import lombok.*;

import javax.persistence.*;

/**
 * Representation of the movie genre.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DiscriminatorValue(value = MovieField.Values.GENRE)
public class MovieGenreEntity extends MovieInfoEntity {

    private static final long serialVersionUID = 5848617668106162823L;

    @Basic
    @Column(name = "genre")
    @Enumerated(EnumType.STRING)
    private GenreType genre;
}
