package com.core.jpa.entity.movie;

import com.common.dto.movie.type.LanguageType;
import com.common.dto.movie.Language;
import com.core.movie.MovieField;
import lombok.*;

import javax.persistence.*;

/**
 * Representation of the movie language.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue(value = MovieField.Values.LANGUAGE)
public class MovieLanguageEntity extends MovieInfoEntity {

    private static final long serialVersionUID = -234056556456996621L;

    @Basic
    @Enumerated(EnumType.STRING)
    private LanguageType language;


    /**
     * Get a DTO representing this box office.
     *
     * @return The read-only DTO.
     */
    public Language getDTO() {
        return Language.builder()
                .language(this.language)
                .build();
    }
}
