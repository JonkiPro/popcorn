package com.jonki.popcorn.web.controller;

import com.jonki.popcorn.common.dto.User;
import com.jonki.popcorn.core.service.UserPersistenceService;
import com.jonki.popcorn.core.service.UserSearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST end-point for supporting User relationships.
 */
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

    /**
     * Add a user to friends.
     *
     * @param username The user's name you want to add to friends. Not null/empty/blank
     */
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

    /**
     * Delete a friend (User) from friends.
     *
     * @param username The user's name you want to delete from friends list. Not null/empty/blank
     */
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

    /**
     * Add a user to the list of invited users to friends.
     *
     * @param username The user's name you want to add to invited users. Not null/empty/blank
     */
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

    /**
     * Delete a sent invitation (User) from sent invitations.
     *
     * @param username The user's name you want to delete from the list of sent invitations to friends.
     *                Not null/empty/blank
     */
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

    /**
     * Delete a received invitation (User) from received invitations.
     *
     * @param username The user's name you want to delete from the list of received invitations to friends.
     *                Not null/empty/blank
     */
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
