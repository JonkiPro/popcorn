package com.jonki.popcorn.web.hateoas.assembler;

import com.jonki.popcorn.common.dto.search.ContributionSearchResult;
import com.jonki.popcorn.web.controller.MovieContributionRestController;
import com.jonki.popcorn.web.hateoas.resource.ContributionSearchResultResource;
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
            case OUTLINE:
                self = ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieContributionRestController.class)
                                .getOutlineContribution(id)
                ).withSelfRel();
                break;
            case SUMMARY:
                self = ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieContributionRestController.class)
                                .getSummaryContribution(id)
                ).withSelfRel();
                break;
            case SYNOPSIS:
                self = ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieContributionRestController.class)
                                .getSynopsisContribution(id)
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
            case PHOTO:
                self = ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieContributionRestController.class)
                                .getPhotoContribution(id)
                ).withSelfRel();
                break;
            case POSTER:
                self = ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieContributionRestController.class)
                                .getPosterContribution(id)
                ).withSelfRel();
                break;
        }

        contributionResource.add(self);

        return contributionResource;
    }
}
