package com.core.jpa.entity.movie;

import com.common.dto.movie.type.CountryType;
import com.common.dto.movie.ReleaseDate;
import com.core.movie.MovieField;
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


    /**
     * Get a DTO representing this release date.
     *
     * @return The read-only DTO.
     */
    public ReleaseDate getDTO() {
        return ReleaseDate.builder()
                .date(this.date)
                .country(this.country)
                .build();
    }
}
