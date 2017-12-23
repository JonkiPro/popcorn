package com.web.web.hateoas.assembler;

import com.common.dto.search.ContributionSearchResult;
import com.web.web.controller.MovieContributionRestController;
import com.web.web.hateoas.resource.ContributionSearchResultResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

/**
 * Assembles Contribution resources out of contribution search result DTOs.
 */
@Component
public class ContributionSearchResultResourceAssembler implements ResourceAssembler<ContributionSearchResult, ContributionSearchResultResource> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ContributionSearchResultResource toResource(final ContributionSearchResult contribution) {
        final ContributionSearchResultResource contributionResource = new ContributionSearchResultResource(contribution);
        final Long id = Long.valueOf(contribution.getId());

        Link self = null;

        switch (contribution.getField()) {
            case OTHER_TITLE:
                self = ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieContributionRestController.class)
                                .getOtherTitleContribution(id)
                ).withSelfRel();
                break;
            case RELEASE_DATE:
                self = ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieContributionRestController.class)
                                .getReleaseDateContribution(id)
                ).withSelfRel();
                break;
            case STORYLINE:
                self = ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieContributionRestController.class)
                                .getStorylineContribution(id)
                ).withSelfRel();
                break;
            case BOX_OFFICE:
                self = ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieContributionRestController.class)
                                .getBoxOfficeContribution(id)
                ).withSelfRel();
                break;
            case SITE:
                self = ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieContributionRestController.class)
                                .getSiteContribution(id)
                ).withSelfRel();
                break;
            case COUNTRY:
                self = ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieContributionRestController.class)
                                .getCountryContribution(id)
                ).withSelfRel();
                break;
            case LANGUAGE:
                self = ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieContributionRestController.class)
                                .getLanguageContribution(id)
                ).withSelfRel();
                break;
            case GENRE:
                self = ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieContributionRestController.class)
                                .getGenreContribution(id)
                ).withSelfRel();
                break;
            case REVIEW:
                self = ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieContributionRestController.class)
                                .getReviewContribution(id)
                ).withSelfRel();
                break;
        }

        contributionResource.add(self);

        return contributionResource;
    }
}
