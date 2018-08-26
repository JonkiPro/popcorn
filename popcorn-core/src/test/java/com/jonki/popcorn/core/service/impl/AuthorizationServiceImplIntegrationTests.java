package com.jonki.popcorn.core.service.impl;

import com.jonki.popcorn.core.jpa.service.DBIntegrationTestBase;
import com.jonki.popcorn.core.security.model.CustomUserDetails;
import com.jonki.popcorn.core.service.AuthorizationService;
import com.jonki.popcorn.test.category.IntegrationTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Integration tests for AuthorizationServiceImpl.
 */
@Category(IntegrationTest.class)
public class AuthorizationServiceImplIntegrationTests extends DBIntegrationTestBase {

    private static final String USER_1_ID = "user1";
    private static final String USER_1_USERNAME = "jonki1";
    private static final String USER_1_PASSWORD = "$2a$12$O02MQJS6AnCJiRw1nF6XsO7udBCza591oUKUbRC4/XrUmPXZaSjVK";

    @Autowired
    private AuthorizationService authorizationService;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        final Authentication authentication = new UsernamePasswordAuthenticationToken(
                new CustomUserDetails(
                        USER_1_USERNAME,
                        USER_1_PASSWORD,
                        AuthorityUtils.createAuthorityList("ROLE_USER"),
                        USER_1_ID
                ),
                null,
                AuthorityUtils.createAuthorityList("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Test the getUserId method.
     */
    @Test
    public void testGetUserId() {
        Assert.assertThat(this.authorizationService.getUserId(), Matchers.is(USER_1_ID));
    }

    /**
     * Test the getUsername method.
     */
    @Test
    public void testGetUsername() {
        Assert.assertThat(this.authorizationService.getUsername(), Matchers.is(USER_1_USERNAME));
    }

    /**
     * Test the isLogged method.
     */
    @Test
    public void testIsLogged() {
        Assert.assertTrue(this.authorizationService.isLogged());
    }
}
