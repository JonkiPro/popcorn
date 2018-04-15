package com.common.dto.movie.request;

import com.common.dto.movie.MovieInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.*;

@Getter
@Setter
@ApiModel(description = "An object representing the contribution to be updated")
public class ContributionUpdate<T extends MovieInfoDTO> {

    @ApiModelProperty(notes = "An updated elements to be added <The ID of the element, An updated element to be added>")
    @Valid
    private Map<Long, T> elementsToAdd;

    @ApiModelProperty(notes = "New elements to be added <A new element>")
    @Valid
    private List<T> newElementsToAdd;

    @ApiModelProperty(notes = "An updated elements to be updated <The ID of the element, An updated element to be updated>")
    @Valid
    private Map<Long, T> elementsToUpdate;

    @ApiModelProperty(notes = "An updated element IDs to be deleted <The ID of the element to be deleted>")
    private Set<Long> idsToDelete;

    @ApiModelProperty(notes = "Sources of information(elements)", required = true)
    @NotEmpty
    private Set<String> sources;

    @ApiModelProperty(notes = "Comment from the user")
    private String comment;

    /**
     * Default constructor. Init all lists, sets and maps.
     */
    public ContributionUpdate() {
        this.elementsToAdd = new HashMap<>();
        this.newElementsToAdd = new ArrayList<>();
        this.elementsToUpdate = new HashMap<>();
        this.idsToDelete = new HashSet<>();
        this.sources = new HashSet<>();
    }
}
