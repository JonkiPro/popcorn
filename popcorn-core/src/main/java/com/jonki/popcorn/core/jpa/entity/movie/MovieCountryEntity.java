package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.MovieField;
import com.jonki.popcorn.common.dto.movie.type.CountryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

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
