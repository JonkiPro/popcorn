package com.jonki.popcorn.core.jpa.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.jonki.popcorn.common.dto.request.ChangeEmailRequest;
import com.jonki.popcorn.common.dto.request.ChangePasswordRequest;
import com.jonki.popcorn.common.dto.request.ForgotPasswordRequest;
import com.jonki.popcorn.common.dto.request.RegisterRequest;
import com.jonki.popcorn.common.exception.ResourceException;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import com.jonki.popcorn.core.security.model.CustomUserDetails;
import com.jonki.popcorn.core.service.UserPersistenceService;
import com.jonki.popcorn.core.service.UserSearchService;
import com.jonki.popcorn.core.util.CollectorUtils;
import com.jonki.popcorn.core.util.EncryptUtils;
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

import java.util.Date;
import java.util.UUID;

/**
 * Integration tests for UserPersistenceServiceImpl.
 */
@Category(IntegrationTest.class)
@DatabaseSetup("UserPersistenceServiceImplIntegrationTests/init.xml")
@DatabaseTearDown("cleanup.xml")
public class UserPersistenceServiceImplIntegrationTests extends DBIntegrationTestBase {

    private static final String USER_USERNAME = UUID.randomUUID().toString();
    private static final String USER_EMAIL = "someemail@someemail.com";
    private static final String USER_PASSWORD = UUID.randomUUID().toString();
    private static final String USER_RE_CAPTCHA = UUID.randomUUID().toString();

    private static final String USER_1_ID = "user1";
    private static final String USER_1_USERNAME = "jonki1";
    private static final String USER_1_EMAIL = "jonki1@gmail.com";
    private static final String USER_1_DECRYPTED_PASSWORD = "password1";
    private static final String USER_1_PASSWORD = "$2a$12$O02MQJS6AnCJiRw1nF6XsO7udBCza591oUKUbRC4/XrUmPXZaSjVK";

    private static final String USER_2_ID = "user2";
    private static final String USER_2_NEW_EMAIL = "newemail@gmail.com";
    private static final String USER_2_EMAIL_CHANGE_TOKEN = "token2";

    private static final String USER_3_ID = "user3";
    private static final String USER_4_ID = "user4";
    private static final String USER_5_ID = "user5";

    private static final String USER_6_ID = "user6";
    private static final String USER_6_ACTIVATION_TOKEN = "token6";

    @Autowired
    private UserPersistenceService userPersistenceService;

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
     * Test the createUser method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateUser() throws ResourceException {
        final RegisterRequest registerRequest = new RegisterRequest.Builder(
                USER_USERNAME,
                USER_EMAIL,
                USER_PASSWORD,
                USER_PASSWORD,
                USER_RE_CAPTCHA
        ).build();
        final Date date = new Date();

        Assert.assertThat(this.userRepository.count(), Matchers.is(6L));
        this.userPersistenceService.createUser(registerRequest);
        Assert.assertThat(this.userRepository.count(), Matchers.is(7L));

        final UserEntity userEntity = this.userRepository.findAll()
                .stream()
                .filter(user -> user.getUsername().equals(USER_USERNAME))
                .collect(CollectorUtils.singletonCollector());

        Assert.assertThat(userEntity.getUsername(), Matchers.is(USER_USERNAME));
        Assert.assertThat(userEntity.getEmail(), Matchers.is(USER_EMAIL));
        Assert.assertTrue(EncryptUtils.matches(USER_PASSWORD, userEntity.getPassword()));
        Assert.assertNotNull(userEntity.getActivationToken());
        Assert.assertNull(userEntity.getEmailChangeToken());
        Assert.assertNull(userEntity.getNewEmail());
        Assert.assertNull(userEntity.getAvatarId().orElse(null));
        Assert.assertNull(userEntity.getAvatarProvider());
        Assert.assertFalse(userEntity.isEnabled());
        Assert.assertTrue(
                date.before(userEntity.getCreated())
                        || date.equals(userEntity.getCreated())
        );
        Assert.assertTrue(
                date.before(userEntity.getModifiedDate())
                        || date.equals(userEntity.getModifiedDate())
        );
        Assert.assertNotNull(userEntity.getUniqueId());
    }


    /**
     * Test the activationUser method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canActivationUser() throws ResourceException {
        this.userPersistenceService.activationUser(USER_6_ACTIVATION_TOKEN);
        final UserEntity userEntity = this.userRepository.findByUniqueIdAndEnabledTrue(USER_6_ID)
                .orElseThrow(IllegalArgumentException::new);
        Assert.assertTrue(userEntity.isEnabled());
    }

    /**
     * Test the resetPassword method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canResetPassword() throws ResourceException {
        final ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest.Builder(
                USER_1_USERNAME,
                USER_1_EMAIL
        ).build();
        this.userPersistenceService.resetPassword(forgotPasswordRequest);
        final UserEntity userEntity = this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID)
                .orElseThrow(IllegalArgumentException::new);
        Assert.assertNotEquals(USER_1_PASSWORD, userEntity.getPassword());
    }

    /**
     * Test the updateNewEmail method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateNewEmail() throws ResourceException {
        final String newEmail = "newemail@gmaiil.com";
        final ChangeEmailRequest changeEmailRequest = new ChangeEmailRequest.Builder(
                USER_1_DECRYPTED_PASSWORD,
                newEmail
        ).build();
        this.userPersistenceService.updateNewEmail(changeEmailRequest);

        final UserEntity userEntity = this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID)
                .orElseThrow(IllegalArgumentException::new);

        Assert.assertNotNull(userEntity.getEmailChangeToken());
        Assert.assertThat(userEntity.getNewEmail(), Matchers.is(newEmail));
    }

    /**
     * Test the updatePassword method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdatePassword() throws ResourceException {
        final String newPassword = "newPassword1";
        final ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest.Builder(
                USER_1_DECRYPTED_PASSWORD,
                newPassword,
                newPassword
        ).build();
        this.userPersistenceService.updatePassword(changePasswordRequest);

        final UserEntity userEntity = this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID)
                .orElseThrow(IllegalArgumentException::new);

        Assert.assertTrue(EncryptUtils.matches(changePasswordRequest.getNewPassword(), userEntity.getPassword()));
    }

    /**
     * Test the updateEmail method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateEmail() throws ResourceException {
        this.userPersistenceService.updateEmail(USER_2_EMAIL_CHANGE_TOKEN);
        final UserEntity userEntity = this.userRepository.findByUniqueIdAndEnabledTrue(USER_2_ID)
                .orElseThrow(IllegalArgumentException::new);

        Assert.assertNull(userEntity.getEmailChangeToken());
        Assert.assertThat(userEntity.getEmail(), Matchers.is(USER_2_NEW_EMAIL));
    }

    /**
     * Test the addFriend method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canAddFriend() throws ResourceException {
        Assert.assertEquals(1, userSearchService.getFriends(USER_1_ID).size());
        this.userPersistenceService.addFriend(USER_4_ID);
        Assert.assertEquals(2, userSearchService.getFriends(USER_1_ID).size());
    }

    /**
     * Test the removeFriend method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canRemoveFriend() throws ResourceException {
        Assert.assertEquals(1, userSearchService.getFriends(USER_1_ID).size());
        this.userPersistenceService.removeFriend(USER_2_ID);
        Assert.assertEquals(0, userSearchService.getFriends(USER_1_ID).size());
    }

    /**
     * Test the addInvitation method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canAddInvitation() throws ResourceException {
        Assert.assertEquals(1, userSearchService.getInvitations(true).size());
        this.userPersistenceService.addInvitation(USER_5_ID);
        Assert.assertEquals(2, userSearchService.getInvitations(true).size());
    }


    /**
     * Test the removeInvitation method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canRemoveInvitation() throws ResourceException {
        Assert.assertEquals(1, userSearchService.getInvitations(true).size());
        this.userPersistenceService.removeInvitation(USER_3_ID);
        Assert.assertEquals(0, userSearchService.getInvitations(true).size());
    }

    /**
     * Test the rejectInvitation method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canRejectInvitation() throws ResourceException {
        Assert.assertEquals(1, userSearchService.getInvitations(false).size());
        this.userPersistenceService.rejectInvitation(USER_4_ID);
        Assert.assertEquals(0, userSearchService.getInvitations(false).size());
    }

    /**
     * Test the createAdmin method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateAdmin() throws ResourceException {
        final RegisterRequest registerRequest = new RegisterRequest.Builder(
                USER_USERNAME,
                USER_EMAIL,
                USER_PASSWORD,
                USER_PASSWORD,
                USER_RE_CAPTCHA
        ).build();

        Assert.assertThat(this.userRepository.count(), Matchers.is(6L));
        this.userPersistenceService.createAdmin(registerRequest);
        Assert.assertThat(this.userRepository.count(), Matchers.is(7L));

        final UserEntity userEntity = this.userRepository.findAll()
                .stream()
                .filter(user -> user.getUsername().equals(USER_USERNAME))
                .collect(CollectorUtils.singletonCollector());

        Assert.assertThat(userEntity.getUsername(), Matchers.is(USER_USERNAME));
        Assert.assertThat(userEntity.getEmail(), Matchers.is(USER_EMAIL));
        Assert.assertTrue(EncryptUtils.matches(USER_PASSWORD, userEntity.getPassword()));
    }
}
