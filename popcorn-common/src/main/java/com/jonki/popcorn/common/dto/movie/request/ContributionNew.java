package com.jonki.popcorn.common.dto.movie.request;

import com.jonki.popcorn.common.dto.movie.MovieInfoDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.*;

@Getter
@Setter
@ApiModel(description = "An object representing a new contribution")
public class ContributionNew<T extends MovieInfoDTO> {

    @ApiModelProperty(notes = "Elements to be added <A new element>")
    @Valid
    private List<T> elementsToAdd;

    @ApiModelProperty(notes = "Elements to be updated <The ID of the updated element, A new element>")
    @Valid
    private Map<Long, T> elementsToUpdate;

    @ApiModelProperty(notes = "Element IDs to be deleted <The ID of the element to be deleted>")
    private Set<Long> idsToDelete;

    @ApiModelProperty(notes = "Sources of information(elements)", required = true)
    @NotEmpty
    private Set<String> sources;

    @ApiModelProperty(notes = "Comment from the user")
    private String comment;

    /**
     * Default constructor. Init all lists, sets and maps.
     */
    public ContributionNew() {
        this.elementsToAdd = new ArrayList<>();
        this.elementsToUpdate = new HashMap<>();
        this.idsToDelete = new HashSet<>();
        this.sources = new HashSet<>();
    }
}
