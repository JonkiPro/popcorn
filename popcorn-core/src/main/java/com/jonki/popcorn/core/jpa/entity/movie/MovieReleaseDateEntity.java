package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.movie.type.CountryType;
import com.jonki.popcorn.common.dto.MovieField;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * Representation of the movie release date.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DiscriminatorValue(value = MovieField.Values.RELEASE_DATE)
public class MovieReleaseDateEntity extends MovieInfoEntity {

    private static final long serialVersionUID = -8305223544901579814L;

    @Basic
    @Column(name = "release_date")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Basic
    @Column(name = "release_date_country")
    @Enumerated(EnumType.STRING)
    private CountryType country;
}
