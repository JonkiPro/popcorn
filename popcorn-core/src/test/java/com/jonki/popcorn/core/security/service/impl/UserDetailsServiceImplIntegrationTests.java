package com.jonki.popcorn.core.security.service.impl;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.jonki.popcorn.common.dto.SecurityRole;
import com.jonki.popcorn.core.jpa.service.DBIntegrationTestBase;
import com.jonki.popcorn.core.security.model.CustomUserDetails;
import com.jonki.popcorn.test.category.IntegrationTest;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for UserDetailsServiceImpl.
 */
@Category(IntegrationTest.class)
@DatabaseSetup("UserDetailsServiceImplIntegrationTests/init.xml")
@DatabaseTearDown("cleanup.xml")
@Transactional
public class UserDetailsServiceImplIntegrationTests extends DBIntegrationTestBase {

    private static final String USER_1_ID = "user1";
    private static final String USER_1_USERNAME = "jonki1";
    private static final String USER_1_PASSWORD = "$2a$12$O02MQJS6AnCJiRw1nF6XsO7udBCza591oUKUbRC4/XrUmPXZaSjVK";

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Test the loadUserByUsername method.
     */
    @Test
    public void canLoadUserByUsername() {
        final CustomUserDetails userDetails
                = (CustomUserDetails) this.userDetailsService.loadUserByUsername(USER_1_USERNAME);

        Assert.assertThat(userDetails.getId(), Matchers.is(USER_1_ID));
        Assert.assertThat(userDetails.getUsername(), Matchers.is(USER_1_USERNAME));
        Assert.assertThat(userDetails.getPassword(), Matchers.is(USER_1_PASSWORD));
        Assert.assertThat(userDetails.getAuthorities(), IsCollectionWithSize.hasSize(1));
        Assert.assertThat(
                userDetails.getAuthorities().toArray()[0].toString(),
                Matchers.is(SecurityRole.ROLE_USER.toString())
        );
    }
}
