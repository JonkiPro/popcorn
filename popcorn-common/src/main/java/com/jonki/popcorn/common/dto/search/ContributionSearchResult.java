package com.jonki.popcorn.common.dto.search;

import com.jonki.popcorn.common.dto.MovieField;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "This class represents the subset of data returned from a Contribution when a search for Contributions is conducted")
public class ContributionSearchResult extends BaseSearchResult {

    private static final long serialVersionUID = 2016504878191043059L;

    @ApiModelProperty(notes = "The movie field", required = true)
    private final MovieField field;

    @ApiModelProperty(notes = "Date of creation of the contribution", required = true)
    private final Date date;

    /**
     * Constructor.
     *
     * @param id The contribution ID
     * @param field The movie field
     * @param date The creation date
     */
    @JsonCreator
    public ContributionSearchResult(
            @NotNull @JsonProperty("id") final Long id,
            @NotNull @JsonProperty("field") final MovieField field,
            @NotNull @JsonProperty("date") final Date date
    ) {
        super(id.toString());
        this.field = field;
        this.date = date;
    }
}
