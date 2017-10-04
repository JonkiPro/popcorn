package com.web.web.security.service.impl;

import com.core.jpa.entity.UserEntity;
import com.core.jpa.repository.UserRepository;
import com.common.dto.SecurityRole;
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
     * @return UserDetails object
     * @throws UsernameNotFoundException No user found
     */
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        log.info("Called with username {}", username);

        Optional<UserEntity> userOptional = userRepository.findByUsernameIgnoreCaseAndEnabledTrue(username);

        userOptional.orElseThrow(() -> new UsernameNotFoundException("No user found with username " + username));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for(SecurityRole role : userOptional.get().getAuthorities()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.toString()));
        }

        return new org.springframework.security.core.userdetails.User(userOptional.get().getUsername(),
                                                                      userOptional.get().getPassword(),
                                                                      grantedAuthorities);
    }
}
