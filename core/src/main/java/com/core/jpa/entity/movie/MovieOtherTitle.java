package com.core.jpa.entity.movie;

import com.common.dto.movie.CountryType;
import com.core.movie.MovieField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Representation of another movie title.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@DiscriminatorValue(value = MovieField.Values.OTHER_TITLE)
public class MovieOtherTitle extends MovieInfo {

    @Column(name = "other_title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "other_title_country")
    private CountryType country;
}
