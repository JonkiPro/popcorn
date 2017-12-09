package com.core.jpa.entity.movie;

import com.common.dto.movie.type.CountryType;
import com.common.dto.movie.OtherTitle;
import com.core.movie.MovieField;
import lombok.*;

import javax.persistence.*;

/**
 * Representation of another movie title.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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


    /**
     * Get a DTO representing this rate.
     *
     * @return The read-only DTO.
     */
    public OtherTitle getDTO() {
        return OtherTitle.builder()
                .title(this.title)
                .country(this.country)
                .build();
    }
}
