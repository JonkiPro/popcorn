package com.jonki.popcorn.web.startup;

import com.jonki.popcorn.common.dto.request.RegisterRequest;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
import com.jonki.popcorn.core.service.UserPersistenceService;
import com.jonki.popcorn.core.service.UserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Initialization of the administrator for the application.
 */
@Component
public class InitAdmin implements ApplicationListener<ApplicationReadyEvent> {

    private final UserPersistenceService userPersistenceService;
    private final UserSearchService userSearchService;

    /**
     * Constructor.
     *
     * @param userPersistenceService The user persistence service to use
     * @param userSearchService The user search service to use
     */
    @Autowired
    public InitAdmin(
            final UserPersistenceService userPersistenceService,
            final UserSearchService userSearchService
    ) {
        this.userPersistenceService = userPersistenceService;
        this.userSearchService = userSearchService;
    }

    // Administrator data
    private final String USERNAME = "JonkiPro";
    private final String PASSWORD = "VeryStrongPassword1";
    private final String EMAIL = "TkM2Gs9Hrd@gmail.com";

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        try {
            userSearchService.getUserByUsername(USERNAME);
        } catch (final ResourceNotFoundException ex) {
            userPersistenceService.createAdmin(initAdmin());
        }
    }

    /**
     * This method initializes the application administrator.
     *
     * @return user (administrator)
     */
    private RegisterRequest initAdmin() {
        return new RegisterRequest.Builder(
                USERNAME, EMAIL, PASSWORD, PASSWORD, null).build();
    }
}
