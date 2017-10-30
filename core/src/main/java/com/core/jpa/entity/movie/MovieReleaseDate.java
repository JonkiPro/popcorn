package com.core.jpa.entity.movie;

import com.common.dto.movie.CountryType;
import com.core.movie.MovieField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Representation of the movie release date.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@DiscriminatorValue(value = MovieField.Values.RELEASE_DATE)
public class MovieReleaseDate extends MovieInfo {

    @Temporal(TemporalType.DATE)
    @Column(name = "release_date")
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "release_date_country")
    private CountryType country;
}
