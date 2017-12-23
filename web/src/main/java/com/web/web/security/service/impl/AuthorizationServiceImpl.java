package com.web.web.security.service.impl;

import com.common.dto.User;
import com.core.service.UserSearchService;
import com.web.web.security.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Implementation of the Authorization Service.
 */
@Service("authorizationService")
public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserSearchService userSearchService;

    /**
     * Constructor.
     *
     * @param userSearchService The user search service to use
     */
    @Autowired
    public AuthorizationServiceImpl(final UserSearchService userSearchService) {
        this.userSearchService = userSearchService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser() {
        return userSearchService.getUserByUsername(this.getUsername());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserId() {
        return userSearchService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsername() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }
}
