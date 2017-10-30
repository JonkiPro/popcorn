package com.core.jpa.entity.movie;

import com.core.movie.MovieField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Representation of the movie review.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@DiscriminatorValue(value = MovieField.Values.REVIEW)
public class MovieReview extends MovieInfo {

    private String review;
}
