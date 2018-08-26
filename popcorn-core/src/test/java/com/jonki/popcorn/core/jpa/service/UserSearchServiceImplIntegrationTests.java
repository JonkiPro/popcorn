package com.jonki.popcorn.core.jpa.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.jonki.popcorn.common.dto.RelationshipStatus;
import com.jonki.popcorn.common.dto.search.UserSearchResult;
import com.jonki.popcorn.common.exception.ResourceException;
import com.jonki.popcorn.core.security.model.CustomUserDetails;
import com.jonki.popcorn.core.service.UserSearchService;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Integration tests for UserSearchServiceImpl.
 */
@Category(IntegrationTest.class)
@DatabaseSetup("UserSearchServiceImplIntegrationTests/init.xml")
@DatabaseTearDown("cleanup.xml")
public class UserSearchServiceImplIntegrationTests extends DBIntegrationTestBase {

    private static final String USER_1_ID = "user1";
    private static final String USER_1_USERNAME = "jonki1";
    private static final String USER_1_EMAIL = "jonki1@gmail.com";
    private static final String USER_1_PASSWORD = "$2a$12$O02MQJS6AnCJiRw1nF6XsO7udBCza591oUKUbRC4/XrUmPXZaSjVK";

    private static final String USER_2_ID = "user2";
    private static final String USER_2_USERNAME = "jonki2";
    private static final String USER_2_EMAIL = "jonki2@gmail.com";

    private static final String USER_3_ID = "user3";
    private static final String USER_4_ID = "user4";
    private static final String USER_5_ID = "user5";

    @Autowired
    private UserSearchService userSearchService;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        Assert.assertThat(this.userRepository.count(), Matchers.is(6L));

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
     * Make sure we can search users successfully.
     */
    @Test
    public void canFindUsers() {
        //TODO: add more cases
        final Pageable page = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        Page<UserSearchResult> users = this.userSearchService
                .findUsers(
                        "j",
                        page
                );
        Assert.assertThat(users.getTotalElements(), Matchers.is(5L));

        users = this.userSearchService
                .findUsers(
                        "JoNkI",
                        page
                );
        Assert.assertThat(users.getTotalElements(), Matchers.is(5L));
        Assert.assertThat(
                users
                        .getContent()
                        .stream()
                        .filter(user -> user.getUsername().equals(USER_1_USERNAME))
                        .count(),
                Matchers.is(1L)
        );

        users = this.userSearchService
                .findUsers(
                        "jonki2",
                        page
                );
        Assert.assertThat(users.getTotalElements(), Matchers.is(1L));
        Assert.assertThat(
                users
                        .getContent()
                        .stream()
                        .filter(user -> user.getUsername().equals(USER_2_USERNAME))
                        .count(),
                Matchers.is(1L)
        );

        users = this.userSearchService
                .findUsers(
                        "qwerty",
                        page
                );
        Assert.assertThat(users.getTotalElements(), Matchers.is(0L));
        Assert.assertTrue(users.getContent().isEmpty());
    }

    /**
     * Test the getUserByUsername method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetUserByUsername() throws ResourceException {
        Assert.assertThat(
                this.userSearchService.getUserByUsername(USER_1_USERNAME).getId(),
                Matchers.is(USER_1_ID)
        );
    }

    /**
     * Test the getUserByEmail method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetUserByEmail() throws ResourceException {
        Assert.assertThat(
                this.userSearchService.getUserByEmail(USER_1_EMAIL).getId(),
                Matchers.is(USER_1_ID)
        );
    }

    /**
     * Test the existsUserByUsername method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void existsUserByUsername() throws ResourceException {
        Assert.assertTrue(this.userSearchService.existsUserByUsername(USER_1_USERNAME));
    }

    /**
     * Test the existsUserByEmail method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void existsUserByEmail() throws ResourceException {
        Assert.assertTrue(this.userSearchService.existsUserByEmail(USER_2_EMAIL));
    }

    /**
     * Test the getUserPassword method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetUserPassword() throws ResourceException {
        Assert.assertThat(
                this.userSearchService.getUserPassword(USER_1_USERNAME),
                Matchers.is(USER_1_PASSWORD)
        );
    }

    /**
     * Test the getFriends method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetFriends() throws ResourceException {
        Assert.assertEquals(2, this.userSearchService.getFriends(USER_1_ID).size());
    }

    /**
     * Test the getInvitations method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetInvitations() throws ResourceException {
        Assert.assertEquals(1, this.userSearchService.getInvitations(true).size());
        Assert.assertEquals(1, this.userSearchService.getInvitations(false).size());
    }

    /**
     * Test the getUserFriendStatus method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetUserFriendStatus() throws ResourceException {
        Assert.assertThat(
                RelationshipStatus.FRIEND,
                Matchers.is(this.userSearchService.getUserFriendStatus(USER_2_ID))
        );
        Assert.assertThat(
                RelationshipStatus.FRIEND,
                Matchers.is(this.userSearchService.getUserFriendStatus(USER_3_ID))
        );
        Assert.assertThat(
                RelationshipStatus.INVITATION_FROM_YOU,
                Matchers.is(this.userSearchService.getUserFriendStatus(USER_4_ID))
        );
        Assert.assertThat(
                RelationshipStatus.INVITATION_TO_YOU,
                Matchers.is(this.userSearchService.getUserFriendStatus(USER_5_ID))
        );
    }
}
