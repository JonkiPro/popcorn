package com.jonki.popcorn.core.security.service.impl;

import com.jonki.popcorn.common.dto.SecurityRole;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import com.jonki.popcorn.core.jpa.repository.UserRepository;
import com.jonki.popcorn.core.security.model.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Authenticate a user from the database. Implementation of the User Details Service.
 */
@Service("userDetailsService")
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructor.
     *
     * @param userRepository The user repository to use
     */
    @Autowired
    public UserDetailsServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Get user by username. Login process.
     *
     * @param username The user's name
     * @return CustomUserDetails {@link CustomUserDetails} object
     * @throws UsernameNotFoundException No user found
     */
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        log.info("Called with username {}", username);

        Optional<UserEntity> userOptional;
        if(username.contains("@")) {
            userOptional = userRepository.findByEmailIgnoreCaseAndEnabledTrue(username);
        } else {
            userOptional = userRepository.findByUsernameIgnoreCaseAndEnabledTrue(username);
        }

        userOptional.orElseThrow(() -> new UsernameNotFoundException("No user found with username/e-mail " + username));

        final Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for(final SecurityRole role : userOptional.get().getAuthorities()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.toString()));
        }

        return new CustomUserDetails(userOptional.get().getUsername(),
                                     userOptional.get().getPassword(),
                                     grantedAuthorities,
                                     userOptional.get().getUniqueId());
    }
}
