package com.common.dto.request;

import com.common.dto.movie.*;
import com.common.dto.request.movie.BoxOffice;
import com.common.dto.request.movie.OtherTitle;
import com.common.dto.request.movie.ReleaseDate;
import com.common.dto.request.movie.Site;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.Set;

@Data
@ApiModel(description = "Data on the new movie")
public class MovieDTO {

    @NotBlank
    @ApiModelProperty(notes = "Title of the movie", required = true)
    private String title;

    @NotNull
    @ApiModelProperty(notes = "Type of movie", required = true)
    private MovieType type;

    @ApiModelProperty(notes = "Another movie title")
    private Set<OtherTitle> otherTitles;

    @ApiModelProperty(notes = "Short description of the movie")
    private String description;

    @ApiModelProperty(notes = "Story line of the movie")
    private String storyline;

    @ApiModelProperty(notes = "The earnings of the movie")
    private Set<BoxOffice> boxOffices;

    @ApiModelProperty(notes = "The release date of the movie")
    private Set<ReleaseDate> releaseDates;

    @ApiModelProperty(notes = "Budget of the movie")
    private String budget;

    @ApiModelProperty(notes = "The website of the movie")
    private Set<Site> sites;

    @ApiModelProperty(notes = "Country of production of the movie")
    private Set<CountryType> countries;

    @ApiModelProperty(notes = "The movie language")
    private Set<LanguageType> languages;

    @ApiModelProperty(notes = "The movie genre")
    private Set<GenreType> genres;

    /**
     * Get the description for the movie if there is one.
     *
     * @return The description
     */
    public Optional<String> getDescription() {
        return Optional.ofNullable(this.description);
    }

    /**
     * Get the budget for the movie if there is one.
     *
     * @return The budget
     */
    public Optional<String> getBudget() {
        return Optional.ofNullable(this.budget);
    }

    /**
     * Get the story line for the movie if there is one.
     *
     * @return The storyline
     */
    public Optional<String> getStoryline() {
        return Optional.ofNullable(this.storyline);
    }
}
