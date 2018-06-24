package com.jonki.popcorn.core.jpa.specification;

import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.MovieField;
import com.jonki.popcorn.core.jpa.entity.ContributionEntity;
import com.jonki.popcorn.core.jpa.entity.ContributionEntity_;
import com.jonki.popcorn.core.jpa.entity.MovieEntity;
import com.jonki.popcorn.test.category.UnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

/**
 * Tests for the contribution specifications.
 */
@Category(UnitTest.class)
public class ContributionSpecsUnitTests {

    private static final MovieEntity MOVIE = Mockito.mock(MovieEntity.class);
    private static final MovieField FIELD = MovieField.SYNOPSIS;
    private static final DataStatus STATUS = DataStatus.ACCEPTED;
    private static final Date FROM_DATE = new Date();
    private static final Date TO_DATE = new Date(new Date().getTime() + (1000 * 60 * 60 * 24));

    private Root<ContributionEntity> root;
    private CriteriaBuilder cb;

    /**
     * Setup some variables.
     */
    @Before
    @SuppressWarnings("unchecked")
    public void setup() {
        this.root = (Root<ContributionEntity>) Mockito.mock(Root.class);
        this.cb = Mockito.mock(CriteriaBuilder.class);

        final Path<MovieEntity> moviePath = (Path<MovieEntity>) Mockito.mock(Path.class);
        final Predicate equalMoviePredicate = Mockito.mock(Predicate.class);
        Mockito.when(this.root.get(ContributionEntity_.movie)).thenReturn(moviePath);
        Mockito.when(this.cb.equal(moviePath, MOVIE)).thenReturn(equalMoviePredicate);

        final Path<MovieField> fieldPath = (Path<MovieField>) Mockito.mock(Path.class);
        final Predicate equalFieldPredicate = Mockito.mock(Predicate.class);
        Mockito.when(this.root.get(ContributionEntity_.field)).thenReturn(fieldPath);
        Mockito.when(this.cb.equal(fieldPath, FIELD)).thenReturn(equalFieldPredicate);

        final Path<DataStatus> statusPath = (Path<DataStatus>) Mockito.mock(Path.class);
        final Predicate equalStatusPredicate = Mockito.mock(Predicate.class);
        Mockito.when(this.root.get(ContributionEntity_.status)).thenReturn(statusPath);
        Mockito.when(this.cb.equal(fieldPath, STATUS)).thenReturn(equalStatusPredicate);

        final Path<Date> fromDatePath = (Path<Date>) Mockito.mock(Path.class);
        final Predicate greaterThanOrEqualToFromDatePredicate = Mockito.mock(Predicate.class);
        Mockito.when(this.root.get(ContributionEntity_.created)).thenReturn(fromDatePath);
        Mockito.when(this.cb.greaterThanOrEqualTo(fromDatePath, FROM_DATE)).thenReturn(greaterThanOrEqualToFromDatePredicate);

        final Path<Date> toDatePath = (Path<Date>) Mockito.mock(Path.class);
        final Predicate lessThanOrEqualToToDatePredicate = Mockito.mock(Predicate.class);
        Mockito.when(this.root.get(ContributionEntity_.created)).thenReturn(toDatePath);
        Mockito.when(this.cb.lessThanOrEqualTo(fromDatePath, TO_DATE)).thenReturn(lessThanOrEqualToToDatePredicate);
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindWithAll() {
        ContributionSpecs.getFindPredicate(
                this.root,
                this.cb,
                MOVIE,
                FIELD,
                STATUS,
                FROM_DATE,
                TO_DATE
        );

        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.root.get(ContributionEntity_.movie), MOVIE);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.root.get(ContributionEntity_.field), FIELD);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.root.get(ContributionEntity_.status), STATUS);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .greaterThanOrEqualTo(this.root.get(ContributionEntity_.created), FROM_DATE);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .lessThanOrEqualTo(this.root.get(ContributionEntity_.created), TO_DATE);
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindWithOutMovie() {
        ContributionSpecs.getFindPredicate(
                this.root,
                this.cb,
                null,
                FIELD,
                STATUS,
                FROM_DATE,
                TO_DATE
        );

        Mockito
                .verify(this.cb, Mockito.never())
                .equal(this.root.get(ContributionEntity_.movie), MOVIE);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.root.get(ContributionEntity_.field), FIELD);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.root.get(ContributionEntity_.status), STATUS);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .greaterThanOrEqualTo(this.root.get(ContributionEntity_.created), FROM_DATE);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .lessThanOrEqualTo(this.root.get(ContributionEntity_.created), TO_DATE);
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindWithOutField() {
        ContributionSpecs.getFindPredicate(
                this.root,
                this.cb,
                MOVIE,
                null,
                STATUS,
                FROM_DATE,
                TO_DATE
        );

        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.root.get(ContributionEntity_.movie), MOVIE);
        Mockito
                .verify(this.cb, Mockito.never())
                .equal(this.root.get(ContributionEntity_.field), FIELD);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.root.get(ContributionEntity_.status), STATUS);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .greaterThanOrEqualTo(this.root.get(ContributionEntity_.created), FROM_DATE);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .lessThanOrEqualTo(this.root.get(ContributionEntity_.created), TO_DATE);
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindWithOutStatus() {
        ContributionSpecs.getFindPredicate(
                this.root,
                this.cb,
                MOVIE,
                FIELD,
                null,
                FROM_DATE,
                TO_DATE
        );

        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.root.get(ContributionEntity_.movie), MOVIE);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.root.get(ContributionEntity_.field), FIELD);
        Mockito
                .verify(this.cb, Mockito.never())
                .equal(this.root.get(ContributionEntity_.status), STATUS);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .greaterThanOrEqualTo(this.root.get(ContributionEntity_.created), FROM_DATE);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .lessThanOrEqualTo(this.root.get(ContributionEntity_.created), TO_DATE);
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindWithOutFromDate() {
        ContributionSpecs.getFindPredicate(
                this.root,
                this.cb,
                MOVIE,
                FIELD,
                STATUS,
                null,
                TO_DATE
        );

        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.root.get(ContributionEntity_.movie), MOVIE);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.root.get(ContributionEntity_.field), FIELD);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.root.get(ContributionEntity_.status), STATUS);
        Mockito
                .verify(this.cb, Mockito.never())
                .greaterThanOrEqualTo(this.root.get(ContributionEntity_.created), FROM_DATE);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .lessThanOrEqualTo(this.root.get(ContributionEntity_.created), TO_DATE);
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindWithOutToDate() {
        ContributionSpecs.getFindPredicate(
                this.root,
                this.cb,
                MOVIE,
                FIELD,
                STATUS,
                FROM_DATE,
                null
        );

        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.root.get(ContributionEntity_.movie), MOVIE);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.root.get(ContributionEntity_.field), FIELD);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .equal(this.root.get(ContributionEntity_.status), STATUS);
        Mockito
                .verify(this.cb, Mockito.times(1))
                .greaterThanOrEqualTo(this.root.get(ContributionEntity_.created), FROM_DATE);
        Mockito
                .verify(this.cb, Mockito.never())
                .lessThanOrEqualTo(this.root.get(ContributionEntity_.created), TO_DATE);
    }
}
