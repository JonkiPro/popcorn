package com.core.jpa.entity.movie;

import com.common.dto.movie.type.CountryType;
import com.common.dto.MovieField;
import lombok.*;

import javax.persistence.*;

/**
 * Representation of another movie title.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@DiscriminatorValue(value = MovieField.Values.OTHER_TITLE)
public class MovieOtherTitleEntity extends MovieInfoEntity {

    private static final long serialVersionUID = 2522515435913741105L;

    @Basic
    @Column(name = "other_title")
    private String title;

    @Basic
    @Column(name = "other_title_country")
    @Enumerated(EnumType.STRING)
    private CountryType country;
}
