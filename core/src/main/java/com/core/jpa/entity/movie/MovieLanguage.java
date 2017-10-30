package com.core.jpa.entity.movie;

import com.common.dto.movie.LanguageType;
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
 * Representation of the movie language.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@DiscriminatorValue(value = MovieField.Values.LANGUAGE)
public class MovieLanguage extends MovieInfo {

    @Enumerated(EnumType.STRING)
    private LanguageType language;
}
