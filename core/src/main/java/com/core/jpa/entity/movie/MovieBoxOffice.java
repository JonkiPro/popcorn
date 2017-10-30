package com.core.jpa.entity.movie;

import com.common.dto.movie.CountryType;
import com.core.movie.MovieField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Representation of the movie box office.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@DiscriminatorValue(value = MovieField.Values.BOX_OFFICE)
public class MovieBoxOffice extends MovieInfo {

    @Column(name = "box_office")
    private String boxOffice;

    @Enumerated(EnumType.STRING)
    @Column(name = "box_office_country")
    private CountryType country;
}
