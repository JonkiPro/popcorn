package com.jonki.popcorn.core.jpa.service;

import com.jonki.popcorn.common.dto.MovieField;
import com.jonki.popcorn.common.exception.ResourceException;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
import com.jonki.popcorn.core.jpa.repository.ContributionRepository;
import com.jonki.popcorn.core.jpa.repository.MovieRepository;
import com.jonki.popcorn.test.category.UnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.Random;

/**
 * Unit tests for MovieContributionSearchServiceImpl.
 */
@Category(UnitTest.class)
public class MovieContributionSearchServiceImplUnitTests {

    private ContributionRepository contributionRepository;
    private MovieContributionSearchServiceImpl service;

    /**
     * Setup for the tests.
     */
    @Before
    public void setup() {
        this.contributionRepository = Mockito.mock(ContributionRepository.class);
        this.service = new MovieContributionSearchServiceImpl(
                this.contributionRepository,
                Mockito.mock(MovieRepository.class)
        );
    }

    /**
     * Test the getOtherTitleContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetOtherTitleContributionIfContributionDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito.when(this.contributionRepository.findByIdAndField(id, MovieField.OTHER_TITLE)).thenReturn(Optional.empty());
        this.service.getOtherTitleContribution(id);
    }

    /**
     * Test the getReleaseDateContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetReleaseDateContributionIfContributionDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito.when(this.contributionRepository.findByIdAndField(id, MovieField.RELEASE_DATE)).thenReturn(Optional.empty());
        this.service.getReleaseDateContribution(id);
    }

    /**
     * Test the getOutlineContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetOutlineContributionIfContributionDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito.when(this.contributionRepository.findByIdAndField(id, MovieField.OUTLINE)).thenReturn(Optional.empty());
        this.service.getOutlineContribution(id);
    }

    /**
     * Test the getSummaryContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetSummaryContributionIfContributionDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito.when(this.contributionRepository.findByIdAndField(id, MovieField.SUMMARY)).thenReturn(Optional.empty());
        this.service.getSummaryContribution(id);
    }

    /**
     * Test the getSynopsisContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetSynopsisContributionIfContributionDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito.when(this.contributionRepository.findByIdAndField(id, MovieField.SYNOPSIS)).thenReturn(Optional.empty());
        this.service.getSynopsisContribution(id);
    }

    /**
     * Test the getBoxOfficeContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetBoxOfficeContributionIfContributionDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito.when(this.contributionRepository.findByIdAndField(id, MovieField.BOX_OFFICE)).thenReturn(Optional.empty());
        this.service.getBoxOfficeContribution(id);
    }

    /**
     * Test the getSiteContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetSiteContributionIfContributionDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito.when(this.contributionRepository.findByIdAndField(id, MovieField.SITE)).thenReturn(Optional.empty());
        this.service.getSiteContribution(id);
    }

    /**
     * Test the getCountryContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetCountryContributionIfContributionDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito.when(this.contributionRepository.findByIdAndField(id, MovieField.COUNTRY)).thenReturn(Optional.empty());
        this.service.getCountryContribution(id);
    }

    /**
     * Test the getLanguageContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetLanguageContributionIfContributionDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito.when(this.contributionRepository.findByIdAndField(id, MovieField.LANGUAGE)).thenReturn(Optional.empty());
        this.service.getLanguageContribution(id);
    }

    /**
     * Test the getGenreContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetGenreContributionIfContributionDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito.when(this.contributionRepository.findByIdAndField(id, MovieField.GENRE)).thenReturn(Optional.empty());
        this.service.getGenreContribution(id);
    }

    /**
     * Test the getReviewContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetReviewContributionIfContributionDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito.when(this.contributionRepository.findByIdAndField(id, MovieField.REVIEW)).thenReturn(Optional.empty());
        this.service.getReviewContribution(id);
    }

    /**
     * Test the getPhotoContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetPhotoContributionIfContributionDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito.when(this.contributionRepository.findByIdAndField(id, MovieField.PHOTO)).thenReturn(Optional.empty());
        this.service.getPhotoContribution(id);
    }

    /**
     * Test the getPosterContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetPosterContributionIfContributionDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito.when(this.contributionRepository.findByIdAndField(id, MovieField.POSTER)).thenReturn(Optional.empty());
        this.service.getPosterContribution(id);
    }
}
