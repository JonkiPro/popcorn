package com.jonki.popcorn.web.hateoas.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.jonki.popcorn.common.dto.search.ContributionSearchResult;
import org.springframework.hateoas.Resource;

/**
 * HATEOAS resource representation of an Contribution.
 */
public class ContributionSearchResultResource extends Resource<ContributionSearchResult> {

    /**
     * Constructor.
     *
     * @param contribution The contribution search result this resource is wrapping
     */
    @JsonCreator
    public ContributionSearchResultResource(final ContributionSearchResult contribution) {
        super(contribution);
    }
}
