package com.jonki.popcorn.core.security.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Custom implementation of User {@link User}.
 */
@Getter
public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = -7087763562429554255L;

    private String id;

    /**
     * Default constructor.
     *
     * @param username The user's name
     * @param password The user's password
     * @param authorities The user's authorities
     */
    public CustomUserDetails(
            final String username,
            final String password,
            final Collection<? extends GrantedAuthority> authorities
    ) {
        super(username, password, authorities);
    }


    /**
     * Default constructor.
     *
     * @param username The user's name
     * @param password The user's password
     * @param enabled The user's enabled
     * @param accountNonExpired Indicates whether the user's account has expired
     * @param credentialsNonExpired Indicates whether the user's credentials (password) has expired
     * @param accountNonLocked Indicates whether the user is locked or unlocked
     * @param authorities The user's authorities
     */
    public CustomUserDetails(
            final String username,
            final String password,
            final boolean enabled,
            final boolean accountNonExpired,
            final boolean credentialsNonExpired,
            final boolean accountNonLocked,
            final Collection<? extends GrantedAuthority> authorities
    ) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    /**
     * Custom constructor.
     *
     * @param username The user's name
     * @param password The user's password
     * @param authorities The user's authorities
     * @param id The user's unique ID
     */
    public CustomUserDetails(
            final String username,
            final String password,
            final Collection<? extends GrantedAuthority> authorities,
            final String id
    ) {
        super(username, password, authorities);
        this.id = id;
    }
}
