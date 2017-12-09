package com.common.dto;

import com.common.dto.movie.MovieInfoDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Getter
@Builder
@ApiModel(description = "An object representing the contribution")
public class Contribution<T extends MovieInfoDTO> {

    @ApiModelProperty(notes = "The contribution ID")
    private Long id;

    @ApiModelProperty(notes = "Elements to be added <The ID of the element, The element>")
    private Map<Long, T> elementsToAdd;

    @ApiModelProperty(notes = "Elements to be updated <The ID of the element, The element>")
    private Map<Long, T> elementsToUpdate;

    @ApiModelProperty(notes = "Elements to be deleted <The ID of the element, The element>")
    private Map<Long, T> elementsToDelete;

    @ApiModelProperty(notes = "Elements updated <The ID of the element, The element>")
    private Map<Long, T> elementsUpdated;

    @ApiModelProperty(notes = "The movie ID")
    private Long movieId;

    @ApiModelProperty(notes = "The title of the movie")
    private String movieTitle;

    @ApiModelProperty(notes = "The name of the contribution author")
    private String username;

    @ApiModelProperty(notes = "The status of contribution")
    private String status;

    @ApiModelProperty(notes = "The movie field")
    private String field;

    @ApiModelProperty(notes = "Sources of information(elements)")
    private Set<String> sources;

    @ApiModelProperty(notes = "Comment from the user(author)")
    private String userComment;

    @ApiModelProperty(notes = "Date of creation of the contribution")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date creationDate;

    @ApiModelProperty(notes = "Date of verification of the contribution")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date verificationDate;

    @ApiModelProperty(notes = "The name of the verification user")
    private String verificationUsername;

    @ApiModelProperty(notes = "The comment of the verification user")
    private String verificationComment;

    /**
     * Default constructor. Init all maps.
     */
    public Contribution() {
        this.elementsToAdd = new HashMap<>();
        this.elementsToUpdate = new HashMap<>();
        this.elementsToDelete = new HashMap<>();
        this.elementsUpdated = new HashMap<>();
    }
}
