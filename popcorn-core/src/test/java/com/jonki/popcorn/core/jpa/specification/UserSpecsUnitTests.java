package com.jonki.popcorn.core.jpa.specification;

import com.jonki.popcorn.core.jpa.entity.UserEntity;
import com.jonki.popcorn.core.jpa.entity.UserEntity_;
import com.jonki.popcorn.test.category.UnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Tests for the user specifications.
 */
@Category(UnitTest.class)
public class UserSpecsUnitTests {

    private static final String USER_NAME = "JonkiPro";

    private Root<UserEntity> root;
    private CriteriaBuilder cb;

    /**
     * Setup some variables.
     */
    @Before
    @SuppressWarnings("unchecked")
    public void setup() {
        this.root = (Root<UserEntity>) Mockito.mock(Root.class);
        this.cb = Mockito.mock(CriteriaBuilder.class);

        final Path<String> userNamePath = (Path<String>) Mockito.mock(Path.class);
        final Predicate likeUserNamePredicate = Mockito.mock(Predicate.class);
        Mockito.when(this.root.get(UserEntity_.username)).thenReturn(userNamePath);
        Mockito.when(this.cb.like(userNamePath, USER_NAME)).thenReturn(likeUserNamePredicate);

        final Path<Boolean> enabledPath = (Path<Boolean>) Mockito.mock(Path.class);
        final Predicate isTrueEnabledPredicate = Mockito.mock(Predicate.class);
        Mockito.when(this.root.get(UserEntity_.enabled)).thenReturn(enabledPath);
        Mockito.when(this.cb.isTrue(enabledPath)).thenReturn(isTrueEnabledPredicate);
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindWithAll() {
        UserSpecs.getFindPredicate(this.root, this.cb, USER_NAME);

        Mockito
                .verify(this.cb, Mockito.times(1))
                .like(this.cb.upper(this.root.get(UserEntity_.username)), "%" + USER_NAME.toUpperCase() + "%");
        Mockito
                .verify(this.cb, Mockito.times(1))
                .isTrue(this.root.get(UserEntity_.enabled));
    }

    /**
     * Test the find specification.
     */
    @Test
    public void testFindWithOutUserName() {
        UserSpecs.getFindPredicate(this.root, this.cb, null);

        Mockito
                .verify(this.cb, Mockito.never())
                .like(this.cb.upper(this.root.get(UserEntity_.username)), "%" + USER_NAME.toUpperCase() + "%");
        Mockito
                .verify(this.cb, Mockito.times(1))
                .isTrue(this.root.get(UserEntity_.enabled));
    }
}
