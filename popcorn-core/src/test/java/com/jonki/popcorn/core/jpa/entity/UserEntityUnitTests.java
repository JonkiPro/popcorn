package com.jonki.popcorn.core.jpa.entity;

import com.google.common.collect.Sets;
import com.jonki.popcorn.common.dto.SecurityRole;
import com.jonki.popcorn.common.dto.StorageProvider;
import com.jonki.popcorn.common.dto.UserMoviePermission;
import com.jonki.popcorn.common.exception.ResourceConflictException;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.validation.ConstraintViolationException;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

/**
 * Test the UserEntity class.
 */
@Category(UnitTest.class)
public class UserEntityUnitTests extends EntityTestsBase {

    private static final String USERNAME = UUID.randomUUID().toString();
    private static final String EMAIL = "TkM2Gs9Hrd@gmail.com";
    private static final String PASSWORD = UUID.randomUUID().toString();
    private static final Set<SecurityRole> AUTHORITIES = Sets.newHashSet(SecurityRole.ROLE_USER);

    private UserEntity u;

    /**
     * Setup the tests.
     */
    @Before
    public void setup() {
        this.u = new UserEntity();
        this.u.setUsername(USERNAME);
        this.u.setEmail(EMAIL);
        this.u.setPassword(PASSWORD);
        this.u.setAuthorities(AUTHORITIES);
    }

    /**
     * Test the default Constructor.
     */
    @Test
    public void testDefaultConstructor() {
        final UserEntity entity = new UserEntity();
        Assert.assertNull(entity.getUsername());
        Assert.assertNull(entity.getEmail());
        Assert.assertNull(entity.getPassword());
        Assert.assertNull(entity.getActivationToken());
        Assert.assertNull(entity.getEmailChangeToken());
        Assert.assertNull(entity.getNewEmail());
        Assert.assertFalse(entity.getAvatarId().isPresent());
        Assert.assertNull(entity.getAvatarProvider());
        Assert.assertFalse(entity.isEnabled());
        Assert.assertTrue(entity.getAuthorities().isEmpty());
        Assert.assertTrue(entity.getSentMessages().isEmpty());
        Assert.assertTrue(entity.getReceivedMessages().isEmpty());
        Assert.assertTrue(entity.getFriends().isEmpty());
        Assert.assertTrue(entity.getSentInvitations().isEmpty());
        Assert.assertTrue(entity.getReceivedInvitations().isEmpty());
        Assert.assertTrue(entity.getPermissions().isEmpty());
        Assert.assertTrue(entity.getRatings().isEmpty());
        Assert.assertTrue(entity.getContributions().isEmpty());
        Assert.assertTrue(entity.getFavoritesMovies().isEmpty());
        Assert.assertNull(entity.getModifiedDate());
    }

    /**
     * Make sure validation works on valid movies.
     */
    @Test
    public void testValidate() {
        this.validate(this.u);
    }

    /**
     * Make sure validation works on with failure from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateUsername() {
        this.u.setUsername("");
        this.validate(this.u);
    }

    /**
     * Make sure validation works on with failure from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateEmail() {
        this.u.setEmail("");
        this.validate(this.u);
    }

    /**
     * Make sure validation works on with failure from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidatePassword() {
        this.u.setPassword("");
        this.validate(this.u);
    }

    /**
     * Make sure validation works on with failure from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateAuthorities() {
        this.u.setAuthorities(Sets.newHashSet());
        this.validate(this.u);
    }

    /**
     * Test validate with exception from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateBadSuperClass() {
        this.validate(new UserEntity());
    }

    /**
     * Test setting the username.
     */
    @Test
    public void testSetUsername() {
        Assert.assertNotNull(this.u.getUsername());
        final String username = UUID.randomUUID().toString();
        this.u.setUsername(username);
        Assert.assertThat(this.u.getUsername(), Matchers.is(username));
    }

    /**
     * Test setting the e-mail.
     */
    @Test
    public void testSetEmail() {
        Assert.assertNotNull(this.u.getEmail());
        final String email = UUID.randomUUID().toString();
        this.u.setEmail(email);
        Assert.assertThat(this.u.getEmail(), Matchers.is(email));
    }

    /**
     * Test setting the password.
     */
    @Test
    public void testSetPassword() {
        Assert.assertNotNull(this.u.getPassword());
        final String password = UUID.randomUUID().toString();
        this.u.setPassword(password);
        Assert.assertThat(this.u.getPassword(), Matchers.is(password));
    }

    /**
     * Test setting the activation token.
     */
    @Test
    public void testSetActivationToken() {
        final String activationToken = UUID.randomUUID().toString();
        this.u.setActivationToken(activationToken);
        Assert.assertThat(this.u.getActivationToken(), Matchers.is(activationToken));
    }

    /**
     * Test setting the e-mail change token.
     */
    @Test
    public void testSetEmailChangeToken() {
        final String emailChangeToken = UUID.randomUUID().toString();
        this.u.setEmailChangeToken(emailChangeToken);
        Assert.assertThat(this.u.getEmailChangeToken(), Matchers.is(emailChangeToken));
    }

    /**
     * Test setting the new e-mail.
     */
    @Test
    public void testSetNewEmail() {
        final String newEmail = UUID.randomUUID().toString();
        this.u.setNewEmail(newEmail);
        Assert.assertThat(this.u.getNewEmail(), Matchers.is(newEmail));
    }

    /**
     * Test setting the avatar ID.
     */
    @Test
    public void testSetAvatarId() {
        final String avatarId = UUID.randomUUID().toString();
        this.u.setAvatarId(avatarId);
        Assert.assertThat(this.u.getAvatarId().orElseGet(RandomSupplier.STRING), Matchers.is(avatarId));
    }

    /**
     * Test setting the avatar provider.
     */
    @Test
    public void testSetAvatarProvider() {
        final StorageProvider avatarProvider = StorageProvider.GOOGLE;
        this.u.setAvatarProvider(avatarProvider);
        Assert.assertThat(this.u.getAvatarProvider(), Matchers.is(avatarProvider));
    }

    /**
     * Test setting the enabled.
     */
    @Test
    public void testSetEnabled() {
        final boolean enabled = true;
        this.u.setEnabled(enabled);
        Assert.assertTrue(this.u.isEnabled());
    }

    /**
     * Test setting the authorities.
     */
    @Test
    public void testSetAuthorities() {
        Assert.assertNotNull(this.u.getAuthorities());
        final Set<SecurityRole> authorities = EnumSet.of(SecurityRole.ROLE_USER);
        this.u.setAuthorities(authorities);
        Assert.assertEquals(authorities, this.u.getAuthorities());
    }

    /**
     * Test to make sure we can add an friend (user).
     *
     * @throws ResourceConflictException on error
     */
    @Test
    public void canAddFriend() throws ResourceConflictException {
        final String id = UUID.randomUUID().toString();
        final UserEntity user = new UserEntity();
        user.setUniqueId(id);

        this.u.addFriend(user);
        Assert.assertThat(this.u.getFriends(), Matchers.hasItem(user));
        Assert.assertThat(user.getFriends(), Matchers.hasItem(this.u));
    }

    /**
     * Test to make sure we can't add an friend (user) to a user if it's already in the list.
     *
     * @throws ResourceConflictException on duplicate
     */
    @Test(expected = ResourceConflictException.class)
    public void cantAddFriendThatAlreadyIsInList() throws ResourceConflictException {
        final String id = UUID.randomUUID().toString();
        final UserEntity user = new UserEntity();
        user.setUniqueId(id);

        this.u.addFriend(user);
        this.u.addFriend(user);
    }

    /**
     * Test removing an friend (user).
     *
     * @throws ResourceNotFoundException if the user is not added to friends
     */
    @Test
    public void canRemoveFriend() throws ResourceNotFoundException {
        final UserEntity one = new UserEntity();
        one.setUniqueId("one");
        final UserEntity two = new UserEntity();
        two.setUniqueId("two");
        Assert.assertNotNull(this.u.getFriends());
        Assert.assertTrue(this.u.getFriends().isEmpty());
        this.u.addFriend(one);
        Assert.assertTrue(this.u.getFriends().contains(one));
        Assert.assertFalse(this.u.getFriends().contains(two));
        Assert.assertTrue(one.getFriends().contains(this.u));
        Assert.assertNotNull(two.getFriends());
        Assert.assertTrue(two.getFriends().isEmpty());
        this.u.addFriend(two);
        Assert.assertTrue(this.u.getFriends().contains(one));
        Assert.assertTrue(this.u.getFriends().contains(two));
        Assert.assertTrue(one.getFriends().contains(this.u));
        Assert.assertTrue(two.getFriends().contains(this.u));

        this.u.removeFriend(one);
        Assert.assertFalse(this.u.getFriends().contains(one));
        Assert.assertTrue(this.u.getFriends().contains(two));
        Assert.assertFalse(one.getFriends().contains(this.u));
        Assert.assertTrue(two.getFriends().contains(this.u));
    }

    /**
     * Test to make sure we can add an sent invitation (user).
     *
     * @throws ResourceConflictException on error
     */
    @Test
    public void canAddSentInvitation() throws ResourceConflictException {
        final String id = UUID.randomUUID().toString();
        final UserEntity user = new UserEntity();
        user.setUniqueId(id);

        this.u.addSentInvitation(user);
        Assert.assertThat(this.u.getSentInvitations(), Matchers.hasItem(user));
        Assert.assertThat(user.getReceivedInvitations(), Matchers.hasItem(this.u));
    }

    /**
     * Test to make sure we can't add an sent invitation (user) to a user if it's already in the list.
     *
     * @throws ResourceConflictException on duplicate or is already added to friends
     */
    @Test(expected = ResourceConflictException.class)
    public void cantAddSentInvitationThatAlreadyIsInList() throws ResourceConflictException {
        final String id = UUID.randomUUID().toString();
        final UserEntity user = new UserEntity();
        user.setUniqueId(id);

        this.u.addSentInvitation(user);
        this.u.addSentInvitation(user);
    }

    /**
     * Test removing an sent invitation (user).
     *
     * @throws ResourceNotFoundException if the user is not added to sent invitations
     */
    @Test
    public void canRemoveSentInvitation() throws ResourceNotFoundException {
        final UserEntity one = new UserEntity();
        one.setUniqueId("one");
        final UserEntity two = new UserEntity();
        two.setUniqueId("two");
        Assert.assertNotNull(this.u.getSentInvitations());
        Assert.assertTrue(this.u.getSentInvitations().isEmpty());
        this.u.addSentInvitation(one);
        Assert.assertTrue(this.u.getSentInvitations().contains(one));
        Assert.assertFalse(this.u.getSentInvitations().contains(two));
        Assert.assertTrue(one.getReceivedInvitations().contains(this.u));
        Assert.assertNotNull(two.getSentInvitations());
        Assert.assertTrue(two.getSentInvitations().isEmpty());
        this.u.addSentInvitation(two);
        Assert.assertTrue(this.u.getSentInvitations().contains(one));
        Assert.assertTrue(this.u.getSentInvitations().contains(two));
        Assert.assertTrue(one.getReceivedInvitations().contains(this.u));
        Assert.assertTrue(two.getReceivedInvitations().contains(this.u));

        this.u.removeSentInvitation(one);
        Assert.assertFalse(this.u.getSentInvitations().contains(one));
        Assert.assertTrue(this.u.getSentInvitations().contains(two));
        Assert.assertFalse(one.getReceivedInvitations().contains(this.u));
        Assert.assertTrue(two.getReceivedInvitations().contains(this.u));
    }

    /**
     * Test setting the permissions.
     */
    @Test
    public void testSetPermissions() {
        Assert.assertNotNull(this.u.getPermissions());
        Assert.assertTrue(this.u.getPermissions().isEmpty());
        final Set<UserMoviePermission> permissions = EnumSet.of(UserMoviePermission.ALL);
        this.u.setPermissions(permissions);
        Assert.assertEquals(permissions, this.u.getPermissions());
    }

    /**
     * Test the toString method.
     */
    @Test
    public void testToString() {
        Assert.assertNotNull(this.u.toString());
    }
}
