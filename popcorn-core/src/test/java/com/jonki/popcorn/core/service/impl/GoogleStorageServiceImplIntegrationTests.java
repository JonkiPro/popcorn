package com.jonki.popcorn.core.service.impl;

import com.jonki.popcorn.common.dto.StorageDirectory;
import com.jonki.popcorn.common.exception.ResourceException;
import com.jonki.popcorn.core.jpa.service.DBIntegrationTestBase;
import com.jonki.popcorn.core.security.model.CustomUserDetails;
import com.jonki.popcorn.core.service.StorageService;
import com.jonki.popcorn.test.category.IntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.File;
import java.util.Objects;

/**
 * Integration tests for GoogleStorageServiceImpl.
 */
@Category(IntegrationTest.class)
public class GoogleStorageServiceImplIntegrationTests extends DBIntegrationTestBase {

    private static final String USER_1_ID = "user1";
    private static final String USER_1_USERNAME = "jonki1";
    private static final String USER_1_PASSWORD = "$2a$12$O02MQJS6AnCJiRw1nF6XsO7udBCza591oUKUbRC4/XrUmPXZaSjVK";

    @Autowired
    @Qualifier("googleStorageService")
    private StorageService storageService;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        final Authentication authentication = new UsernamePasswordAuthenticationToken(
                new CustomUserDetails(
                        USER_1_USERNAME,
                        USER_1_PASSWORD,
                        AuthorityUtils.createAuthorityList("ROLE_USER"),
                        USER_1_ID
                ),
                null,
                AuthorityUtils.createAuthorityList("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Test the save method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canSave() throws ResourceException {
        this.storageService.save(
                new File(
                        Objects.requireNonNull(
                                this.getClass().getClassLoader().getResource("test-image.png")
                        ).getFile()
                ),
                StorageDirectory.IMAGE
        );
    }
}
