package com.core.jpa.entity.movie;

import com.common.dto.movie.type.CountryType;
import com.common.dto.movie.Country;
import com.core.movie.MovieField;
import lombok.*;

import javax.persistence.*;

/**
 * Representation of the movie country.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue(value = MovieField.Values.COUNTRY)
public class MovieCountryEntity extends MovieInfoEntity {

    private static final long serialVersionUID = -5543998902825941481L;

    @Basic
    @Enumerated(EnumType.STRING)
    private CountryType country;


    /**
     * Get a DTO representing this box office.
     *
     * @return The read-only DTO.
     */
    public Country getDTO() {
        return Country.builder()
                .country(this.country)
                .build();
    }
}
