package com.core.service.impl;

import com.core.security.model.CustomUserDetails;
import com.core.service.AuthorizationService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Implementation of the Authorization Service.
 */
@Service("authorizationService")
public class AuthorizationServiceImpl implements AuthorizationService {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserId() {
        return ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsername() {
        return ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLogged() {
        return SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                //when Anonymous Authentication is enabled
                && !(SecurityContextHolder.getContext().getAuthentication()
                        instanceof AnonymousAuthenticationToken);
    }
}
