package com.jonki.popcorn.core.security.model;

import com.jonki.popcorn.common.dto.SecurityRole;
import com.jonki.popcorn.core.security.model.CustomUserDetails;
import com.jonki.popcorn.core.util.CollectorUtils;
import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Tests for the CustomUserDetails class.
 */
@Category(UnitTest.class)
public class CustomUserDetailsUnitTests {

    private static final String USER_USERNAME = UUID.randomUUID().toString();
    private static final String USER_PASSWORD = UUID.randomUUID().toString();
    private static final Set<GrantedAuthority> USER_AUTHORITIES
            = new HashSet<>(Collections.singletonList(new SimpleGrantedAuthority(SecurityRole.ROLE_USER.toString())));

    /**
     * Constructor test with three arguments.
     */
    @Test
    public void testConstructorWithThreeArgs() {
        final CustomUserDetails customUserDetails = new CustomUserDetails(
                USER_USERNAME,
                USER_PASSWORD,
                USER_AUTHORITIES
        );

        Assert.assertThat(customUserDetails.getUsername(), Matchers.is(USER_USERNAME));
        Assert.assertThat(customUserDetails.getPassword(), Matchers.is(USER_PASSWORD));
        Assert.assertEquals(
                customUserDetails.getAuthorities().stream().collect(CollectorUtils.singletonCollector()).getAuthority(),
                SecurityRole.ROLE_USER.toString()
        );
    }

    /**
     * Constructor test with seven arguments.
     */
    @Test
    public void testConstructorWithSevenArgs() {
        final CustomUserDetails customUserDetails = new CustomUserDetails(
                USER_USERNAME,
                USER_PASSWORD,
                true,
                false,
                true,
                false,
                USER_AUTHORITIES
        );

        Assert.assertThat(customUserDetails.getUsername(), Matchers.is(USER_USERNAME));
        Assert.assertThat(customUserDetails.getPassword(), Matchers.is(USER_PASSWORD));
        Assert.assertTrue(customUserDetails.isEnabled());
        Assert.assertFalse(customUserDetails.isAccountNonExpired());
        Assert.assertTrue(customUserDetails.isCredentialsNonExpired());
        Assert.assertFalse(customUserDetails.isAccountNonLocked());
        Assert.assertEquals(
                customUserDetails.getAuthorities().stream().collect(CollectorUtils.singletonCollector()).getAuthority(),
                SecurityRole.ROLE_USER.toString()
        );
    }

    /**
     * Constructor test with four arguments.
     */
    @Test
        public void testConstructorWithFourArgs() {
        final String userId = "1";
        final CustomUserDetails customUserDetails = new CustomUserDetails(
                USER_USERNAME,
                USER_PASSWORD,
                USER_AUTHORITIES,
                userId

        );

        Assert.assertThat(customUserDetails.getUsername(), Matchers.is(USER_USERNAME));
        Assert.assertThat(customUserDetails.getPassword(), Matchers.is(USER_PASSWORD));
        Assert.assertEquals(
                customUserDetails.getAuthorities().stream().collect(CollectorUtils.singletonCollector()).getAuthority(),
                SecurityRole.ROLE_USER.toString()
        );
        Assert.assertThat(customUserDetails.getId(), Matchers.is(userId));
    }
}
