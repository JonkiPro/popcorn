package com.web.web.hateoas.resource;

import com.common.dto.search.ContributionSearchResult;
import com.fasterxml.jackson.annotation.JsonCreator;
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
