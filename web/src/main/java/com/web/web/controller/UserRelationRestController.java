package com.web.web.controller;

import com.common.dto.User;
import com.core.service.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/api/v1.0/relations")
@Slf4j
@Api(value = "User Relation API", description = "Provides a list of methods that manage user relationships")
public class UserRelationRestController {

    private final UserSearchService userSearchService;
    private final UserPersistenceService userPersistenceService;

    /**
     * Constructor.
     *
     * @param userSearchService The user search service to use
     * @param userPersistenceService The user persistence service to use
     */
    @Autowired
    public UserRelationRestController(
            final UserSearchService userSearchService,
            final UserPersistenceService userPersistenceService
    ) {
        this.userSearchService = userSearchService;
        this.userPersistenceService = userPersistenceService;
    }

    @ApiOperation(value = "Add friend")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No user found or no invitation found"),
            @ApiResponse(code = 409, message = "The friendship exists")
    })
    @PostMapping(value = "/friends/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void addFriend(
            @ApiParam(value = "The user's name", required = true)
            @PathVariable("username") final String username
    ) {
        log.info("Called with username {}", username);

        final User user = this.userSearchService.getUserByUsername(username);

        this.userPersistenceService.addFriend(user.getId());
    }

    @ApiOperation(value = "Remove friend")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No user found or no friendship found") })
    @DeleteMapping(value = "/friends/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void removeFriend(
            @ApiParam(value = "The user's name", required = true)
            @PathVariable("username") final String username
    ) {
        log.info("Called with username {}", username);

        final User user = this.userSearchService.getUserByUsername(username);

        this.userPersistenceService.removeFriend(user.getId());
    }

    @ApiOperation(value = "Create an invitation")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No user found"),
            @ApiResponse(code = 409, message = "The invitation exists")
    })
    @PostMapping(value = "/invitations/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void addInvitation(
            @ApiParam(value = "The user's name", required = true)
            @PathVariable("username") final String username
    ) {
        log.info("Called with username {}", username);

        final User user = this.userSearchService.getUserByUsername(username);

        this.userPersistenceService.addInvitation(user.getId());
    }

    @ApiOperation(value = "Delete invitation")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No user found or no invitation found") })
    @DeleteMapping(value = "/invitations/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void removeInvitation(
            @ApiParam(value = "The user's name", required = true)
            @PathVariable("username") final String username
    ) {
        log.info("Called with username {}", username);

        final User user = this.userSearchService.getUserByUsername(username);

        this.userPersistenceService.removeInvitation(user.getId());
    }

    @ApiOperation(value = "Reject invitation")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No user found or no invitation found") })
    @DeleteMapping(value = "/invitations/{username}/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void rejectInvitation(
            @ApiParam(value = "The user's name", required = true)
            @PathVariable("username") final String username
    ) {
        log.info("Called with username {}", username);

        final User user = this.userSearchService.getUserByUsername(username);

        this.userPersistenceService.rejectInvitation(user.getId());
    }
}
