package com.web.web.startup;

import com.common.dto.request.RegisterDTO;
import com.common.exception.ResourceNotFoundException;
import com.core.jpa.service.UserPersistenceService;
import com.core.jpa.service.UserSearchService;
import com.core.utils.EncryptUtils;
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
    public void onApplicationEvent(ApplicationReadyEvent event) {

        try {
            userSearchService.getUserByUsername(USERNAME);
        } catch (ResourceNotFoundException ex) {
            userPersistenceService.createAdmin(initAdmin());
        }
    }

    /**
     * This method initializes the application administrator.
     *
     * @return user (administrator)
     */
    private RegisterDTO initAdmin() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername(USERNAME);
        registerDTO.setPassword(PASSWORD);
        registerDTO.setPasswordAgain(PASSWORD);
        registerDTO.setEmail(EMAIL);

        return registerDTO;
    }
}
