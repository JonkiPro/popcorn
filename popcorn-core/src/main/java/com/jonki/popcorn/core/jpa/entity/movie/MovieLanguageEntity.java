package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.movie.type.LanguageType;
import com.jonki.popcorn.common.dto.MovieField;
import lombok.*;

import javax.persistence.*;

/**
 * Representation of the movie language.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DiscriminatorValue(value = MovieField.Values.LANGUAGE)
public class MovieLanguageEntity extends MovieInfoEntity {

    private static final long serialVersionUID = -234056556456996621L;

    @Basic
    @Column(name = "language")
    @Enumerated(EnumType.STRING)
    private LanguageType language;
}
