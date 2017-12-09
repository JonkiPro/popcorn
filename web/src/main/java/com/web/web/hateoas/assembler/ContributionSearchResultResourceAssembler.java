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
        final Long id = contribution.getId();

        Link self = null;

        if(contribution.getField().equals("OTHER_TITLE")) {
            self = ControllerLinkBuilder.linkTo(
                    ControllerLinkBuilder
                            .methodOn(MovieContributionRestController.class)
                            .getOtherTitleContribution(id)
            ).withSelfRel();
        } else if(contribution.getField().equals("RELEASE_DATE")) {
            self = ControllerLinkBuilder.linkTo(
                    ControllerLinkBuilder
                            .methodOn(MovieContributionRestController.class)
                            .getReleaseDateContribution(id)
            ).withSelfRel();
        } else if(contribution.getField().equals("STORYLINE")) {
            self = ControllerLinkBuilder.linkTo(
                    ControllerLinkBuilder
                            .methodOn(MovieContributionRestController.class)
                            .getStorylineContribution(id)
            ).withSelfRel();
        } else if(contribution.getField().equals("BOX_OFFICE")) {
            self = ControllerLinkBuilder.linkTo(
                    ControllerLinkBuilder
                            .methodOn(MovieContributionRestController.class)
                            .getBoxOfficeContribution(id)
            ).withSelfRel();
        } else if(contribution.getField().equals("SITE")) {
            self = ControllerLinkBuilder.linkTo(
                    ControllerLinkBuilder
                            .methodOn(MovieContributionRestController.class)
                            .getSiteContribution(id)
            ).withSelfRel();
        } else if(contribution.getField().equals("COUNTRY")) {
            self = ControllerLinkBuilder.linkTo(
                    ControllerLinkBuilder
                            .methodOn(MovieContributionRestController.class)
                            .getCountryContribution(id)
            ).withSelfRel();
        } else if(contribution.getField().equals("LANGUAGE")) {
            self = ControllerLinkBuilder.linkTo(
                    ControllerLinkBuilder
                            .methodOn(MovieContributionRestController.class)
                            .getLanguageContribution(id)
            ).withSelfRel();
        } else if(contribution.getField().equals("GENRE")) {
            self = ControllerLinkBuilder.linkTo(
                    ControllerLinkBuilder
                            .methodOn(MovieContributionRestController.class)
                            .getGenreContribution(id)
            ).withSelfRel();
        } else if(contribution.getField().equals("REVIEW")) {
            self = ControllerLinkBuilder.linkTo(
                    ControllerLinkBuilder
                            .methodOn(MovieContributionRestController.class)
                            .getReviewContribution(id)
            ).withSelfRel();
        }

        contributionResource.add(self);

        return contributionResource;
    }
}
