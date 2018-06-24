package com.jonki.popcorn.core.jpa.service;

import com.jonki.popcorn.common.dto.SecurityRole;
import com.jonki.popcorn.common.dto.UserMoviePermission;
import com.jonki.popcorn.common.dto.request.ChangeEmailRequest;
import com.jonki.popcorn.common.dto.request.ChangePasswordRequest;
import com.jonki.popcorn.common.dto.request.ForgotPasswordRequest;
import com.jonki.popcorn.common.dto.request.RegisterRequest;
import com.jonki.popcorn.common.exception.ResourceBadRequestException;
import com.jonki.popcorn.common.exception.ResourceConflictException;
import com.jonki.popcorn.common.exception.ResourceException;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import com.jonki.popcorn.core.jpa.repository.UserRepository;
import com.jonki.popcorn.core.service.AuthorizationService;
import com.jonki.popcorn.core.service.MailService;
import com.jonki.popcorn.core.service.StorageService;
import com.jonki.popcorn.core.util.EncryptUtils;
import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.dao.DuplicateKeyException;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

/**
 * Unit tests for UserPersistenceServiceImpl.
 */
@Category(UnitTest.class)
public class UserPersistenceServiceImplUnitTests {

    private static final String USER_1_ID = UUID.randomUUID().toString();
    private static final String USER_1_USERNAME = UUID.randomUUID().toString();
    private static final String USER_1_EMAIL = UUID.randomUUID().toString();
    private static final String USER_1_PASSWORD= UUID.randomUUID().toString();
    private static final String USER_1_RE_CAPTCHA = UUID.randomUUID().toString();

    private UserRepository userRepository;
    private AuthorizationService authorizationService;
    private UserPersistenceServiceImpl userPersistenceService;

    /**
     * Setup for the tests.
     */
    @Before
    public void setup() {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.authorizationService = Mockito.mock(AuthorizationService.class);
        this.userPersistenceService = new UserPersistenceServiceImpl(
                this.userRepository,
                Mockito.mock(MailService.class),
                Mockito.mock(StorageService.class),
                this.authorizationService
        );
    }

    /**
     * Test the createUser method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void testCreateUser() throws ResourceException {
        final RegisterRequest registerRequest = new RegisterRequest.Builder(
                USER_1_USERNAME,
                USER_1_EMAIL,
                USER_1_PASSWORD,
                USER_1_PASSWORD,
                USER_1_RE_CAPTCHA
        ).build();

        final ArgumentCaptor<UserEntity> argument = ArgumentCaptor.forClass(UserEntity.class);
        this.userPersistenceService.createUser(registerRequest);
        Mockito.verify(this.userRepository).save(argument.capture());

        Assert.assertEquals(USER_1_USERNAME, argument.getValue().getUsername());
        Assert.assertEquals(USER_1_EMAIL, argument.getValue().getEmail());
        Assert.assertTrue(EncryptUtils.matches(USER_1_PASSWORD, argument.getValue().getPassword()));
        Assert.assertNotNull(argument.getValue().getUniqueId());
        Assert.assertNotNull(argument.getValue().getActivationToken());
        Assert.assertThat(argument.getValue().getAuthorities(), IsIterableContainingInAnyOrder.containsInAnyOrder(SecurityRole.ROLE_USER));
    }

    /**
     * Test the createUser method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceConflictException.class)
    public void testCreateUserAlreadyExists() throws ResourceException {
        final RegisterRequest registerRequest = new RegisterRequest.Builder(
                USER_1_USERNAME,
                USER_1_EMAIL,
                USER_1_PASSWORD,
                USER_1_PASSWORD,
                USER_1_RE_CAPTCHA
        ).build();

        Mockito
                .when(this.userRepository.save(Mockito.any(UserEntity.class)))
                .thenThrow(new DuplicateKeyException("Duplicate Key"));
        this.userPersistenceService.createUser(registerRequest);
    }

    /**
     * Test the activationUser method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canActivationUser() throws ResourceException {
        final String activationToken = UUID.randomUUID().toString();
        final UserEntity entity = new UserEntity();
        Mockito
                .when(this.userRepository.findByActivationToken(activationToken))
                .thenReturn(Optional.of(entity));
        this.userPersistenceService.activationUser(activationToken);

        Assert.assertNull(entity.getActivationToken());
        Assert.assertTrue(entity.isEnabled());
    }

    /**
     * Test the activationUser method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantActivationUserIfUserWithActivationTokenDoesNotExist() throws ResourceException {
        final String activationToken = UUID.randomUUID().toString();
        Mockito
                .when(this.userRepository.findByActivationToken(activationToken))
                .thenReturn(Optional.empty());
        this.userPersistenceService.activationUser(activationToken);
    }

    /**
     * Test the resetPassword method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canResetPassword() throws ResourceException {
        final UserEntity entity = Mockito.mock(UserEntity.class);
        final ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest.Builder(
                USER_1_USERNAME,
                USER_1_EMAIL
        ).build();
        Mockito
                .when(this.userRepository.findByUsernameIgnoreCaseAndEmailIgnoreCaseAndEnabledTrue(USER_1_USERNAME, USER_1_EMAIL))
                .thenReturn(Optional.of(entity));
        this.userPersistenceService.resetPassword(forgotPasswordRequest);
    }

    /**
     * Test the resetPassword method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantResetPasswordIfUserWithUsernameAndEmailDoesNotExist() throws ResourceException {
        final ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest.Builder(
                USER_1_USERNAME,
                USER_1_EMAIL
        ).build();
        Mockito
                .when(this.userRepository.findByUsernameIgnoreCaseAndEmailIgnoreCaseAndEnabledTrue(USER_1_USERNAME, USER_1_EMAIL))
                .thenReturn(Optional.empty());
        this.userPersistenceService.resetPassword(forgotPasswordRequest);
    }

    /**
     * Test the updateNewEmail method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateNewEmail() throws ResourceException {
        final UserEntity entity = new UserEntity();
        final ChangeEmailRequest changeEmailRequest = new ChangeEmailRequest.Builder(
                USER_1_PASSWORD,
                USER_1_EMAIL
        ).build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(USER_1_ID);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID))
                .thenReturn(Optional.of(entity));
        Mockito
                .when(this.userRepository.existsByEmailIgnoreCase(USER_1_EMAIL))
                .thenReturn(false);
        this.userPersistenceService.updateNewEmail(changeEmailRequest);

        Assert.assertNotNull(entity.getEmailChangeToken());
        Assert.assertNotNull(entity.getNewEmail());
    }

    /**
     * Test the updateNewEmail method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceConflictException.class)
    public void cantUpdateNewEmailIfUserWithEmailExist() throws ResourceException {
        final UserEntity entity = new UserEntity();
        final ChangeEmailRequest changeEmailRequest = new ChangeEmailRequest.Builder(
                USER_1_PASSWORD,
                USER_1_EMAIL
        ).build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(USER_1_ID);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID))
                .thenReturn(Optional.of(entity));
        Mockito
                .when(this.userRepository.existsByEmailIgnoreCase(USER_1_EMAIL))
                .thenReturn(true);
        this.userPersistenceService.updateNewEmail(changeEmailRequest);
    }

    /**
     * Test the updatePassword method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdatePassword() throws ResourceException {
        final UserEntity entity = new UserEntity();
        final String oldPassword = UUID.randomUUID().toString();
        entity.setPassword(EncryptUtils.encrypt(oldPassword));
        final ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest.Builder(
                oldPassword,
                USER_1_PASSWORD,
                USER_1_PASSWORD
        ).build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(USER_1_ID);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID))
                .thenReturn(Optional.of(entity));
        this.userPersistenceService.updatePassword(changePasswordRequest);

        Assert.assertTrue(EncryptUtils.matches(changePasswordRequest.getNewPassword(), entity.getPassword()));
    }

    /**
     * Test the updatePassword method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceBadRequestException.class)
    public void cantUpdatePasswordIfUserHasAnotherOldPassword() throws ResourceException {
        final UserEntity entity = new UserEntity();
        final String oldPassword = UUID.randomUUID().toString();
        entity.setPassword(EncryptUtils.encrypt(oldPassword));
        final ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest.Builder(
                UUID.randomUUID().toString(),
                USER_1_PASSWORD,
                USER_1_PASSWORD
        ).build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(USER_1_ID);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID))
                .thenReturn(Optional.of(entity));
        this.userPersistenceService.updatePassword(changePasswordRequest);
    }

    /**
     * Test the updateAvatar method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = NullPointerException.class)
    public void cantUpdateAvatarIfFileNull() throws ResourceException {
        this.userPersistenceService.updateAvatar(Mockito.mock(File.class));
    }

    /**
     * Test the updateEmail method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateEmail() throws ResourceException {
        final UserEntity entity = new UserEntity();
        final String emailChangeToken = UUID.randomUUID().toString();
        Mockito
                .when(this.userRepository.findByEmailChangeToken(emailChangeToken))
                .thenReturn(Optional.of(entity));
        this.userPersistenceService.updateEmail(emailChangeToken);

        Assert.assertNull(entity.getEmailChangeToken());
        Assert.assertNull(entity.getNewEmail());
    }

    /**
     * Test the updateEmail method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantUpdateEmailIfUserWithEmailChangeTokenDoesNotExist() throws ResourceException {
        final String emailChangeToken = UUID.randomUUID().toString();
        Mockito
                .when(this.userRepository.findByEmailChangeToken(emailChangeToken))
                .thenReturn(Optional.empty());
        this.userPersistenceService.updateEmail(emailChangeToken);
    }

    /**
     * Test the addFriend method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canAddFriend() throws ResourceException {
        final UserEntity entity1 = new UserEntity();
        entity1.setUniqueId(USER_1_ID);
        final UserEntity entity2 = new UserEntity();
        entity2.setUniqueId(UUID.randomUUID().toString());
        entity2.addSentInvitation(entity1);
        Mockito.when(this.authorizationService.getUserId()).thenReturn(USER_1_ID);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID))
                .thenReturn(Optional.of(entity1));
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(entity2.getUniqueId()))
                .thenReturn(Optional.of(entity2));
        this.userPersistenceService.addFriend(entity2.getUniqueId());
    }

    /**
     * Test the addFriend method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceConflictException.class)
    public void cantAddFriendIfAlreadyAdded() throws ResourceException {
        final UserEntity entity1 = new UserEntity();
        entity1.setUniqueId(USER_1_ID);
        final UserEntity entity2 = new UserEntity();
        entity2.setUniqueId(UUID.randomUUID().toString());
        entity1.addFriend(entity1);
        Mockito.when(this.authorizationService.getUserId()).thenReturn(USER_1_ID);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID))
                .thenReturn(Optional.of(entity1));
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(entity2.getUniqueId()))
                .thenReturn(Optional.of(entity2));
        this.userPersistenceService.addFriend(entity2.getUniqueId());
    }

    /**
     * Test the removeFriend method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canRemoveFriend() throws ResourceException {
        final UserEntity entity1 = new UserEntity();
        entity1.setUniqueId(USER_1_ID);
        final UserEntity entity2 = new UserEntity();
        entity2.setUniqueId(UUID.randomUUID().toString());
        entity2.addSentInvitation(entity1);
        Mockito.when(this.authorizationService.getUserId()).thenReturn(USER_1_ID);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID))
                .thenReturn(Optional.of(entity1));
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(entity2.getUniqueId()))
                .thenReturn(Optional.of(entity2));
        this.userPersistenceService.addFriend(entity2.getUniqueId());
        this.userPersistenceService.removeFriend(entity2.getUniqueId());
    }

    /**
     * Test the removeFriend method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantRemoveFriendIfNotAdded() throws ResourceException {
        final UserEntity entity1 = new UserEntity();
        entity1.setUniqueId(USER_1_ID);
        final UserEntity entity2 = new UserEntity();
        entity2.setUniqueId(UUID.randomUUID().toString());
        Mockito.when(this.authorizationService.getUserId()).thenReturn(USER_1_ID);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID))
                .thenReturn(Optional.of(entity1));
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(entity2.getUniqueId()))
                .thenReturn(Optional.of(entity2));
        this.userPersistenceService.removeFriend(entity2.getUniqueId());
    }

    /**
     * Test the addInvitation method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canAddInvitation() throws ResourceException {
        final UserEntity entity1 = new UserEntity();
        entity1.setUniqueId(USER_1_ID);
        final UserEntity entity2 = new UserEntity();
        entity2.setUniqueId(UUID.randomUUID().toString());
        Mockito.when(this.authorizationService.getUserId()).thenReturn(USER_1_ID);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID))
                .thenReturn(Optional.of(entity1));
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(entity2.getUniqueId()))
                .thenReturn(Optional.of(entity2));
        this.userPersistenceService.addInvitation(entity2.getUniqueId());
    }

    /**
     * Test the addInvitation method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceConflictException.class)
    public void cantAddInvitationIfAlreadyAdded() throws ResourceException {
        final UserEntity entity1 = new UserEntity();
        entity1.setUniqueId(USER_1_ID);
        final UserEntity entity2 = new UserEntity();
        entity2.setUniqueId(UUID.randomUUID().toString());
        entity1.addSentInvitation(entity2);
        Mockito.when(this.authorizationService.getUserId()).thenReturn(USER_1_ID);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID))
                .thenReturn(Optional.of(entity1));
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(entity2.getUniqueId()))
                .thenReturn(Optional.of(entity2));
        this.userPersistenceService.addInvitation(entity2.getUniqueId());
    }

    /**
     * Test the addInvitation method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceConflictException.class)
    public void cantAddInvitationIfAlreadyAddedToFriends() throws ResourceException {
        final UserEntity entity1 = new UserEntity();
        entity1.setUniqueId(USER_1_ID);
        final UserEntity entity2 = new UserEntity();
        entity2.setUniqueId(UUID.randomUUID().toString());
        entity1.addFriend(entity2);
        Mockito.when(this.authorizationService.getUserId()).thenReturn(USER_1_ID);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID))
                .thenReturn(Optional.of(entity1));
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(entity2.getUniqueId()))
                .thenReturn(Optional.of(entity2));
        this.userPersistenceService.addInvitation(entity2.getUniqueId());
    }

    /**
     * Test the removeInvitation method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canRemoveInvitation() throws ResourceException {
        final UserEntity entity1 = new UserEntity();
        entity1.setUniqueId(USER_1_ID);
        final UserEntity entity2 = new UserEntity();
        entity2.setUniqueId(UUID.randomUUID().toString());
        Mockito.when(this.authorizationService.getUserId()).thenReturn(USER_1_ID);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID))
                .thenReturn(Optional.of(entity1));
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(entity2.getUniqueId()))
                .thenReturn(Optional.of(entity2));
        this.userPersistenceService.addInvitation(entity2.getUniqueId());
        this.userPersistenceService.removeInvitation(entity2.getUniqueId());
    }

    /**
     * Test the removeInvitation method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantRemoveInvitationIfNotAdded() throws ResourceException {
        final UserEntity entity1 = new UserEntity();
        entity1.setUniqueId(USER_1_ID);
        final UserEntity entity2 = new UserEntity();
        entity2.setUniqueId(UUID.randomUUID().toString());
        Mockito.when(this.authorizationService.getUserId()).thenReturn(USER_1_ID);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID))
                .thenReturn(Optional.of(entity1));
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(entity2.getUniqueId()))
                .thenReturn(Optional.of(entity2));
        this.userPersistenceService.removeInvitation(entity2.getUniqueId());
    }

    /**
     * Test the rejectInvitation method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canRejectInvitation() throws ResourceException {
        final UserEntity entity1 = new UserEntity();
        entity1.setUniqueId(USER_1_ID);
        final UserEntity entity2 = new UserEntity();
        entity2.setUniqueId(UUID.randomUUID().toString());
        entity2.addSentInvitation(entity1);
        Mockito.when(this.authorizationService.getUserId()).thenReturn(USER_1_ID);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID))
                .thenReturn(Optional.of(entity1));
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(entity2.getUniqueId()))
                .thenReturn(Optional.of(entity2));
        this.userPersistenceService.rejectInvitation(entity2.getUniqueId());
    }

    /**
     * Test the rejectInvitation method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantRejectInvitationIfNotAdded() throws ResourceException {
        final UserEntity entity1 = new UserEntity();
        entity1.setUniqueId(USER_1_ID);
        final UserEntity entity2 = new UserEntity();
        entity2.setUniqueId(UUID.randomUUID().toString());
        Mockito.when(this.authorizationService.getUserId()).thenReturn(USER_1_ID);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID))
                .thenReturn(Optional.of(entity1));
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(entity2.getUniqueId()))
                .thenReturn(Optional.of(entity2));
        this.userPersistenceService.rejectInvitation(entity2.getUniqueId());
    }

    /**
     * Test the createAdmin method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void testCreateAdmin() throws ResourceException {
        final RegisterRequest registerRequest = new RegisterRequest.Builder(
                USER_1_USERNAME,
                USER_1_EMAIL,
                USER_1_PASSWORD,
                USER_1_PASSWORD,
                USER_1_RE_CAPTCHA
        ).build();

        final ArgumentCaptor<UserEntity> argument = ArgumentCaptor.forClass(UserEntity.class);
        this.userPersistenceService.createAdmin(registerRequest);
        Mockito.verify(this.userRepository).save(argument.capture());

        Assert.assertEquals(USER_1_USERNAME, argument.getValue().getUsername());
        Assert.assertEquals(USER_1_EMAIL, argument.getValue().getEmail());
        Assert.assertTrue(EncryptUtils.matches(USER_1_PASSWORD, argument.getValue().getPassword()));
        Assert.assertNotNull(argument.getValue().getUniqueId());
        Assert.assertThat(argument.getValue().getAuthorities(), IsIterableContainingInAnyOrder.containsInAnyOrder(SecurityRole.ROLE_USER, SecurityRole.ROLE_ADMIN));
        Assert.assertThat(argument.getValue().getPermissions(), IsIterableContainingInAnyOrder.containsInAnyOrder(UserMoviePermission.ALL));
    }

    /**
     * Test the createAdmin method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceConflictException.class)
    public void testCreateAdminAlreadyExists() throws ResourceException {
        final RegisterRequest registerRequest = new RegisterRequest.Builder(
                USER_1_USERNAME,
                USER_1_EMAIL,
                USER_1_PASSWORD,
                USER_1_PASSWORD,
                USER_1_RE_CAPTCHA
        ).build();

        Mockito
                .when(this.userRepository.save(Mockito.any(UserEntity.class)))
                .thenThrow(new DuplicateKeyException("Duplicate Key"));
        this.userPersistenceService.createAdmin(registerRequest);
    }
}
