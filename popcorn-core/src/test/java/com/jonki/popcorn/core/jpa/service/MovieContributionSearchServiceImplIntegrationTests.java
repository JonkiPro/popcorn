package com.jonki.popcorn.core.jpa.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.jonki.popcorn.common.dto.Contribution;
import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.MovieField;
import com.jonki.popcorn.common.dto.movie.BoxOffice;
import com.jonki.popcorn.common.dto.movie.Country;
import com.jonki.popcorn.common.dto.movie.Genre;
import com.jonki.popcorn.common.dto.movie.Language;
import com.jonki.popcorn.common.dto.movie.Outline;
import com.jonki.popcorn.common.dto.movie.ReleaseDate;
import com.jonki.popcorn.common.dto.movie.Review;
import com.jonki.popcorn.common.dto.movie.Site;
import com.jonki.popcorn.common.dto.movie.Summary;
import com.jonki.popcorn.common.dto.movie.Synopsis;
import com.jonki.popcorn.common.dto.movie.response.ImageResponse;
import com.jonki.popcorn.common.dto.search.ContributionSearchResult;
import com.jonki.popcorn.common.exception.ResourceException;
import com.jonki.popcorn.core.service.MovieContributionSearchService;
import com.jonki.popcorn.test.category.IntegrationTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Calendar;

/**
 * Integration tests for MovieContributionSearchServiceImpl.
 */
@Category(IntegrationTest.class)
@DatabaseSetup("MovieContributionSearchServiceImplIntegrationTests/init.xml")
@DatabaseTearDown("cleanup.xml")
public class MovieContributionSearchServiceImplIntegrationTests extends DBIntegrationTestBase {

    @Autowired
    private MovieContributionSearchService movieContributionSearchService;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        Assert.assertThat(this.contributionRepository.count(), Matchers.is(14L));
    }

    /**
     * Make sure we can search contributions successfully.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canFindContributions() throws ResourceException {
        //TODO: add more cases
        final Pageable page = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        Page<ContributionSearchResult> contributions = this.movieContributionSearchService
                .findContributions(
                        null,
                        null,
                        null,
                        null,
                        null,
                        page
                );
        Assert.assertThat(contributions.getTotalElements(), Matchers.is(14L));

        contributions = this.movieContributionSearchService
                .findContributions(
                        2L,
                        null,
                        null,
                        null,
                        null,
                        page
                );
        Assert.assertThat(contributions.getTotalElements(), Matchers.is(12L));

        contributions = this.movieContributionSearchService
                .findContributions(
                        null,
                        MovieField.RELEASE_DATE,
                        null,
                        null,
                        null,
                        page
                );
        Assert.assertThat(contributions.getTotalElements(), Matchers.is(2L));

        contributions = this.movieContributionSearchService
                .findContributions(
                        null,
                        null,
                        DataStatus.ACCEPTED,
                        null,
                        null,
                        page
                );
        Assert.assertThat(contributions.getTotalElements(), Matchers.is(3L));

        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2018);
        cal.set(Calendar.MONTH, Calendar.AUGUST);
        cal.set(Calendar.DAY_OF_MONTH, 13);

        contributions = this.movieContributionSearchService
                .findContributions(
                        null,
                        null,
                        null,
                        cal.getTime(),
                        null,
                        page
                );
        Assert.assertThat(contributions.getTotalElements(), Matchers.is(1L));

        contributions = this.movieContributionSearchService
                .findContributions(
                        null,
                        null,
                        null,
                        null,
                        cal.getTime(),
                        page
                );
        Assert.assertThat(contributions.getTotalElements(), Matchers.is(13L));
    }

    /**
     * Test the getReleaseDateContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetReleaseDateContribution() throws ResourceException {
        final Contribution<ReleaseDate> contribution = this.movieContributionSearchService.getReleaseDateContribution(2L);

        Assert.assertThat(contribution.getId(), Matchers.is("2"));
        Assert.assertThat(contribution.getElementsToAdd().size(), Matchers.is(1));
    }

    /**
     * Test the getOutlineContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetOutlineContribution() throws ResourceException {
        final Contribution<Outline> contribution = this.movieContributionSearchService.getOutlineContribution(3L);

        Assert.assertThat(contribution.getId(), Matchers.is("3"));
        Assert.assertThat(contribution.getElementsToAdd().size(), Matchers.is(1));
    }

    /**
     * Test the getSummaryContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetSummaryContribution() throws ResourceException {
        final Contribution<Summary> contribution = this.movieContributionSearchService.getSummaryContribution(4L);

        Assert.assertThat(contribution.getId(), Matchers.is("4"));
        Assert.assertThat(contribution.getElementsToAdd().size(), Matchers.is(1));
    }

    /**
     * Test the getSynopsisContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetSynopsisContribution() throws ResourceException {
        final Contribution<Synopsis> contribution = this.movieContributionSearchService.getSynopsisContribution(5L);

        Assert.assertThat(contribution.getId(), Matchers.is("5"));
        Assert.assertThat(contribution.getElementsToAdd().size(), Matchers.is(1));
    }

    /**
     * Test the getBoxOfficeContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetBoxOfficeContribution() throws ResourceException {
        final Contribution<BoxOffice> contribution = this.movieContributionSearchService.getBoxOfficeContribution(6L);

        Assert.assertThat(contribution.getId(), Matchers.is("6"));
        Assert.assertThat(contribution.getElementsToAdd().size(), Matchers.is(1));
    }

    /**
     * Test the getSiteContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetSiteContribution() throws ResourceException {
        final Contribution<Site> contribution = this.movieContributionSearchService.getSiteContribution(7L);

        Assert.assertThat(contribution.getId(), Matchers.is("7"));
        Assert.assertThat(contribution.getElementsToAdd().size(), Matchers.is(1));
    }

    /**
     * Test the getCountryContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetCountryContribution() throws ResourceException {
        final Contribution<Country> contribution = this.movieContributionSearchService.getCountryContribution(8L);

        Assert.assertThat(contribution.getId(), Matchers.is("8"));
        Assert.assertThat(contribution.getElementsToAdd().size(), Matchers.is(1));
    }

    /**
     * Test the getLanguageContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetLanguageContribution() throws ResourceException {
        final Contribution<Language> contribution = this.movieContributionSearchService.getLanguageContribution(9L);

        Assert.assertThat(contribution.getId(), Matchers.is("9"));
        Assert.assertThat(contribution.getElementsToAdd().size(), Matchers.is(1));
    }

    /**
     * Test the getGenreContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetGenreContribution() throws ResourceException {
        final Contribution<Genre> contribution = this.movieContributionSearchService.getGenreContribution(10L);

        Assert.assertThat(contribution.getId(), Matchers.is("10"));
        Assert.assertThat(contribution.getElementsToAdd().size(), Matchers.is(1));
    }

    /**
     * Test the getReviewContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetReviewContribution() throws ResourceException {
        final Contribution<Review> contribution = this.movieContributionSearchService.getReviewContribution(11L);

        Assert.assertThat(contribution.getId(), Matchers.is("11"));
        Assert.assertThat(contribution.getElementsToAdd().size(), Matchers.is(1));
    }

    /**
     * Test the getPhotoContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetPhotoContribution() throws ResourceException {
        final Contribution<ImageResponse> contribution = this.movieContributionSearchService.getPhotoContribution(12L);

        Assert.assertThat(contribution.getId(), Matchers.is("12"));
        Assert.assertThat(contribution.getElementsToAdd().size(), Matchers.is(1));
    }

    /**
     * Test the getPosterContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetPosterContribution() throws ResourceException {
        final Contribution<ImageResponse> contribution = this.movieContributionSearchService.getPosterContribution(13L);

        Assert.assertThat(contribution.getId(), Matchers.is("13"));
        Assert.assertThat(contribution.getElementsToAdd().size(), Matchers.is(1));
    }
}
