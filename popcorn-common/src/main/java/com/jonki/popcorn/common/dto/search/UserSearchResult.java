package com.jonki.popcorn.common.dto.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * This class represents the subset of data returned from a User when a search for Users is conducted.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "This class represents the subset of data returned from a User when a search for Users is conducted")
public class UserSearchResult extends BaseSearchResult {

    private static final long serialVersionUID = -4475701415263228688L;

    @ApiModelProperty(notes = "The user's name", required = true)
    private final String username;

    @ApiModelProperty(notes = "The user's e-mail", required = true)
    private final String email;

    /**
     * Constructor.
     *
     * @param id The user ID
     * @param username The user's name
     * @param email The user's e-mail
     */
    @JsonCreator
    public UserSearchResult(
            @NotBlank @JsonProperty("id") final String id,
            @NotBlank @JsonProperty("username") final String username,
            @NotBlank @JsonProperty("email") final String email
    ) {
        super(id);
        this.username = username;
        this.email = email;
    }
}
