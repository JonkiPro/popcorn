package com.jonki.popcorn.core.jpa.service;

import com.jonki.popcorn.common.dto.User;
import com.jonki.popcorn.common.exception.ResourceException;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import com.jonki.popcorn.core.jpa.repository.UserRepository;
import com.jonki.popcorn.core.service.AuthorizationService;
import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

/**
 * Unit tests for UserSearchServiceImpl.
 */
@Category(UnitTest.class)
public class UserSearchServiceImplUnitTests {

    private UserRepository userRepository;
    private AuthorizationService authorizationService;
    private UserSearchServiceImpl userSearchService;

    /**
     * Setup for the tests.
     */
    @Before
    public void setup() {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.authorizationService = Mockito.mock(AuthorizationService.class);
        this.userSearchService = new UserSearchServiceImpl(
                this.userRepository,
                this.authorizationService
        );
    }

    /**
     * Test the getUserByUsername method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetUserByUsername() throws ResourceException {
        final UserEntity entity = new UserEntity();
        final String id = UUID.randomUUID().toString();
        final String username = UUID.randomUUID().toString();
        entity.setUniqueId(id);
        Mockito
                .when(this.userRepository.findByUsernameIgnoreCaseAndEnabledTrue(username))
                .thenReturn(Optional.of(entity));
        final User returnedUser = this.userSearchService.getUserByUsername(username);
        Mockito
                .verify(this.userRepository, Mockito.times(1))
                .findByUsernameIgnoreCaseAndEnabledTrue(username);
        Assert.assertThat(returnedUser.getId(), Matchers.is(id));
    }

    /**
     * Test the getUserByUsername method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetUserByEmailIfUserDoesNotExist() throws ResourceException {
        final String username = UUID.randomUUID().toString();
        Mockito.when(this.userRepository.findByUsernameIgnoreCaseAndEnabledTrue(username)).thenReturn(Optional.empty());
        this.userSearchService.getUserByUsername(username);
    }

    /**
     * Test the getUserByEmail method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetUserByEmail() throws ResourceException {
        final UserEntity entity = new UserEntity();
        final String id = UUID.randomUUID().toString();
        final String email = UUID.randomUUID().toString();
        entity.setUniqueId(id);
        Mockito
                .when(this.userRepository.findByEmailIgnoreCaseAndEnabledTrue(email))
                .thenReturn(Optional.of(entity));
        final User returnedUser = this.userSearchService.getUserByEmail(email);
        Mockito
                .verify(this.userRepository, Mockito.times(1))
                .findByEmailIgnoreCaseAndEnabledTrue(email);
        Assert.assertThat(returnedUser.getId(), Matchers.is(id));
    }

    /**
     * Test the getUserByEmail method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantUserByEmailIfUserDoesNotExist() throws ResourceException {
        final String email = UUID.randomUUID().toString();
        Mockito.when(this.userRepository.findByEmailIgnoreCaseAndEnabledTrue(email)).thenReturn(Optional.empty());
        this.userSearchService.getUserByEmail(email);
    }

    /**
     * Test the existsUserByUsername method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void existsUserByUsername() throws ResourceException {
        final String username = UUID.randomUUID().toString();
        Mockito.when(this.userRepository.existsByUsernameIgnoreCase(username)).thenReturn(true);
        Assert.assertTrue(this.userSearchService.existsUserByUsername(username));
    }

    /**
     * Test the existsUserByUsername method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void notExistsUserByUsername() throws ResourceException {
        final String username = UUID.randomUUID().toString();
        Mockito.when(this.userRepository.existsByUsernameIgnoreCase(username)).thenReturn(false);
        Assert.assertFalse(this.userSearchService.existsUserByUsername(username));
    }

    /**
     * Test the existsUserByEmail method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void existsUserByEmail() throws ResourceException {
        final String email = UUID.randomUUID().toString();
        Mockito.when(this.userRepository.existsByEmailIgnoreCase(email)).thenReturn(true);
        Assert.assertTrue(this.userSearchService.existsUserByEmail(email));
    }

    /**
     * Test the existsUserByEmail method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void notExistsUserByEmail() throws ResourceException {
        final String email = UUID.randomUUID().toString();
        Mockito.when(this.userRepository.existsByEmailIgnoreCase(email)).thenReturn(false);
        Assert.assertFalse(this.userSearchService.existsUserByEmail(email));
    }

    /**
     * Test the getUserPassword method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetUserPassword() throws ResourceException {
        final UserEntity entity = new UserEntity();
        final String username = UUID.randomUUID().toString();
        final String password = UUID.randomUUID().toString();
        entity.setPassword(password);
        Mockito
                .when(this.userRepository.findByUsernameIgnoreCaseAndEnabledTrue(username))
                .thenReturn(Optional.of(entity));
        final String returnedUserPassword = this.userSearchService.getUserPassword(username);
        Assert.assertThat(returnedUserPassword, Matchers.is(password));
    }

    /**
     * Test the getUserPassword method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetUserPasswordIfUserDoesNotExist() throws ResourceException {
        final String username = UUID.randomUUID().toString();
        Mockito.when(this.userRepository.findByUsernameIgnoreCaseAndEnabledTrue(username)).thenReturn(Optional.empty());
        this.userSearchService.getUserPassword(username);
    }

    /**
     * Test the getFriends method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetFriends() throws ResourceException {
        final String id = UUID.randomUUID().toString();
        final UserEntity entity = Mockito.mock(UserEntity.class);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(id))
                .thenReturn(Optional.of(entity));
        Assert.assertTrue(this.userSearchService.getFriends(id).isEmpty());
    }

    /**
     * Test the getFriends method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetFriendsIfUserDoesNotExist() throws ResourceException {
        final String id = UUID.randomUUID().toString();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(id);
        this.userSearchService.getFriends(id);
    }

    /**
     * Test the getInvitations method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetInvitations() throws ResourceException {
        final String id = "1";
        final UserEntity entity = Mockito.mock(UserEntity.class);
        Mockito.when(this.authorizationService.getUserId()).thenReturn(id);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(id))
                .thenReturn(Optional.of(entity));
        Assert.assertTrue(this.userSearchService.getInvitations(true).isEmpty());
        Assert.assertTrue(this.userSearchService.getInvitations(false).isEmpty());
    }

    /**
     * Test the getInvitations method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetInvitationsIfUserDoesNotExist() throws ResourceException {
        final String id = UUID.randomUUID().toString();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(id);
        this.userSearchService.getInvitations(true);
        this.userSearchService.getInvitations(false);
    }

    /**
     * Test the getUserFriendStatus method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetUserFriendStatusIfUserDoesNotExist() throws ResourceException {
        final String id = UUID.randomUUID().toString();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(id);
        this.userSearchService.getUserFriendStatus(id);
    }
}
