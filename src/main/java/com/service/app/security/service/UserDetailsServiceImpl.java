package com.service.app.security.service;

import com.service.app.entity.User;
import com.service.app.exception.ResourceNotFoundException;
import com.service.app.repository.UserRepository;
import com.service.app.security.role.SecurityRole;
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
 * Authenticate a user from the database.
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsernameIgnoreCaseAndEnabledTrue(username);

        userOptional.orElseThrow(() -> new ResourceNotFoundException("Incorrect data!"));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for(SecurityRole role : userOptional.get().getAuthorities()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.toString()));
        }

        return new org.springframework.security.core.userdetails.User(userOptional.get().getUsername(),
                                                                      userOptional.get().getPassword(),
                                                                      grantedAuthorities);
    }
}
