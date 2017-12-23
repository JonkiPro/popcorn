package com.core.jpa.entity.movie;

import com.common.dto.movie.type.CountryType;
import com.common.dto.MovieField;
import lombok.*;

import javax.persistence.*;

/**
 * Representation of the movie country.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DiscriminatorValue(value = MovieField.Values.COUNTRY)
public class MovieCountryEntity extends MovieInfoEntity {

    private static final long serialVersionUID = -5543998902825941481L;

    @Basic
    @Column(name = "country")
    @Enumerated(EnumType.STRING)
    private CountryType country;
}
