package com.jonki.popcorn.core.security.service.impl;

import com.jonki.popcorn.common.dto.SecurityRole;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import com.jonki.popcorn.core.jpa.repository.UserRepository;
import com.jonki.popcorn.core.security.model.CustomUserDetails;
import com.jonki.popcorn.core.security.service.impl.UserDetailsServiceImpl;
import com.jonki.popcorn.core.util.CollectorUtils;
import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

/**
 * Tests for the UserDetailsServiceImpl class.
 */
@Category(UnitTest.class)
public class UserDetailsServiceImplUnitTests {

    private static final String USER_ID = UUID.randomUUID().toString();
    private static final String USER_USERNAME = UUID.randomUUID().toString();
    private static final String USER_EMAIL = UUID.randomUUID().toString() + "@";
    private static final String USER_PASSWORD = UUID.randomUUID().toString();
    private static final EnumSet<SecurityRole> USER_AUTHORITIES = EnumSet.of(SecurityRole.ROLE_USER);

    private UserRepository userRepository;
    private UserDetailsServiceImpl service;

    /**
     * Setup for the tests.
     */
    @Before
    public void setup() {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.service = new UserDetailsServiceImpl(this.userRepository);
    }

    /**
     * Test of the user load by name.
     *
     * @throws UsernameNotFoundException If user not found
     */
    @Test
    public void canLoadUserByUsername() throws UsernameNotFoundException {
        final UserEntity userEntity = new UserEntity();
        userEntity.setUniqueId(USER_ID);
        userEntity.setUsername(USER_USERNAME);
        userEntity.setPassword(USER_PASSWORD);
        userEntity.setAuthorities(USER_AUTHORITIES);

        Mockito
                .when(userRepository.findByUsernameIgnoreCaseAndEnabledTrue(USER_USERNAME))
                .thenReturn(Optional.of(userEntity));
        final CustomUserDetails customUserDetails = (CustomUserDetails) this.service.loadUserByUsername(USER_USERNAME);

        Assert.assertThat(customUserDetails.getId(), Matchers.is(USER_ID));
        Assert.assertThat(customUserDetails.getUsername(), Matchers.is(USER_USERNAME));
        Assert.assertThat(customUserDetails.getPassword(), Matchers.is(USER_PASSWORD));
        Assert.assertEquals(
                customUserDetails.getAuthorities().stream().collect(CollectorUtils.singletonCollector()).getAuthority(),
                SecurityRole.ROLE_USER.toString()
        );
    }

    /**
     * Test of the user load by name.
     *
     * @throws UsernameNotFoundException If user not found
     */
    @Test(expected = UsernameNotFoundException.class)
    public void cantLoadUserByUsernameIfUserDoesNotExist() throws UsernameNotFoundException {
        final UserEntity userEntity = new UserEntity();
        userEntity.setUniqueId(USER_ID);
        userEntity.setUsername(USER_USERNAME);
        userEntity.setPassword(USER_PASSWORD);
        userEntity.setAuthorities(USER_AUTHORITIES);

        Mockito
                .when(userRepository.findByUsernameIgnoreCaseAndEnabledTrue(USER_USERNAME))
                .thenReturn(Optional.empty());
        this.service.loadUserByUsername(USER_USERNAME);
    }

    /**
     * Test of the user load by e-mail.
     *
     * @throws UsernameNotFoundException If user not found
     */
    @Test
    public void canLoadUserByEmail() throws UsernameNotFoundException {
        final UserEntity userEntity = new UserEntity();
        userEntity.setUniqueId(USER_ID);
        userEntity.setUsername(USER_USERNAME);
        userEntity.setEmail(USER_EMAIL);
        userEntity.setPassword(USER_PASSWORD);
        userEntity.setAuthorities(USER_AUTHORITIES);

        Mockito
                .when(userRepository.findByEmailIgnoreCaseAndEnabledTrue(USER_EMAIL))
                .thenReturn(Optional.of(userEntity));
        final CustomUserDetails customUserDetails = (CustomUserDetails) this.service.loadUserByUsername(USER_EMAIL);

        Assert.assertThat(customUserDetails.getId(), Matchers.is(USER_ID));
        Assert.assertThat(customUserDetails.getUsername(), Matchers.is(USER_USERNAME));
        Assert.assertThat(customUserDetails.getPassword(), Matchers.is(USER_PASSWORD));
        Assert.assertEquals(
                customUserDetails.getAuthorities().stream().collect(CollectorUtils.singletonCollector()).getAuthority(),
                SecurityRole.ROLE_USER.toString()
        );
    }

    /**
     * Test of the user load by e-mail.
     *
     * @throws UsernameNotFoundException If user not found
     */
    @Test(expected = UsernameNotFoundException.class)
    public void cantLoadUserByEmailIfUserDoesNotExist() throws UsernameNotFoundException {
        final UserEntity userEntity = new UserEntity();
        userEntity.setUniqueId(USER_ID);
        userEntity.setUsername(USER_USERNAME);
        userEntity.setEmail(USER_EMAIL);
        userEntity.setPassword(USER_PASSWORD);
        userEntity.setAuthorities(USER_AUTHORITIES);

        Mockito
                .when(userRepository.findByEmailIgnoreCaseAndEnabledTrue(USER_EMAIL))
                .thenReturn(Optional.empty());
        this.service.loadUserByUsername(USER_EMAIL);
    }
}
