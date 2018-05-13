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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
