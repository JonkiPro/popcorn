package com.web.web.controller;

import com.common.dto.User;
import com.common.exception.ResourceConflictException;
import com.common.exception.ResourceNotFoundException;
import com.core.jpa.service.*;
import com.common.exception.ResourceForbiddenException;
import com.common.dto.RelationshipStatus;
import com.web.web.security.service.AuthorizationService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/api/v1.0/relations")
@Slf4j
@Api(value = "UserEntity Relation API", description = "Provides a list of methods that manage user relationships")
public class UserRelationRestController {

    private final UserSearchService userSearchService;
    private final AuthorizationService authorizationService;
    private final InvitationPersistenceService invitationPersistenceService;
    private final InvitationSearchService invitationSearchService;
    private final FriendshipPersistenceService friendshipPersistenceService;
    private final FriendshipSearchService friendshipSearchService;

    /**
     * Constructor.
     *
     * @param userSearchService The user search service to use
     * @param authorizationService The authorization service to use
     * @param invitationPersistenceService The invitation persistence service to use
     * @param invitationSearchService The invitation search service to use
     * @param friendshipPersistenceService The friendship persistence service to use
     * @param friendshipSearchService The friendship search service to use
     */
    @Autowired
    public UserRelationRestController(
            final UserSearchService userSearchService,
            final AuthorizationService authorizationService,
            final InvitationPersistenceService invitationPersistenceService,
            final InvitationSearchService invitationSearchService,
            final FriendshipPersistenceService friendshipPersistenceService,
            final FriendshipSearchService friendshipSearchService
    ) {
        this.userSearchService = userSearchService;
        this.authorizationService = authorizationService;
        this.invitationPersistenceService = invitationPersistenceService;
        this.invitationSearchService = invitationSearchService;
        this.friendshipPersistenceService = friendshipPersistenceService;
        this.friendshipSearchService = friendshipSearchService;
    }

    @ApiOperation(value = "Save friendship")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No user found or no invitation found"),
            @ApiResponse(code = 403, message = "Forbidden command"),
            @ApiResponse(code = 409, message = "The friendship exists")
    })
    @PostMapping("/friends/{username}")
    public
    HttpEntity<Boolean> saveFriendship(
            @ApiParam(value = "The user's name", required = true) @PathVariable final String username
    ) {
        log.info("Called with username {}", username);

        this.validUsername(username);

        // Get an authorized user
        final User fromUser = this.authorizationService.getUser();
        // Get the user by username
        final User toUser = this.userSearchService.getUserByUsername(username);

        // Delete the invitation
        this.invitationPersistenceService.deleteInvitation(toUser.getId(), fromUser.getId());
        // Save the friendship
        this.friendshipPersistenceService.createFriendship(fromUser.getId(), toUser.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(true);
    }

    @ApiOperation(value = "Remove friendship")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No user found or no friendship found"),
            @ApiResponse(code = 403, message = "Forbidden command")
    })
    @DeleteMapping("/friends/{username}")
    public
    HttpEntity<Boolean> removeFriendship(
            @ApiParam(value = "The user's name", required = true) @PathVariable final String username
    ) {
        log.info("Called with username {}", username);

        this.validUsername(username);

        // Get an authorized user
        final User fromUser = this.authorizationService.getUser();
        // Get the user by username
        final User toUser = this.userSearchService.getUserByUsername(username);

        // Delete the friendship
        this.friendshipPersistenceService.deleteFriendship(fromUser.getId(), toUser.getId());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(true);
    }

    @ApiOperation(value = "Save invitation")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No user found"),
            @ApiResponse(code = 403, message = "Forbidden command"),
            @ApiResponse(code = 409, message = "The invitation exists")
    })
    @PostMapping("/invitations/{username}")
    public
    HttpEntity<Boolean> sendInvitation(
            @ApiParam(value = "The user's name", required = true) @PathVariable final String username
    ) {
        log.info("Called with username {}", username);

        this.validUsername(username);

        // Get an authorized user
        final User fromUser = this.authorizationService.getUser();
        // Get the user by username
        final User toUser = this.userSearchService.getUserByUsername(username);

        // Save the invitation
        this.invitationPersistenceService.createInvitation(fromUser.getId(), toUser.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(true);
    }

    @ApiOperation(value = "Remove/reject invitation")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No user found or no invitation found"),
            @ApiResponse(code = 403, message = "Forbidden command")
    })
    @DeleteMapping("/invitations/{username}")
    public
    HttpEntity<Boolean> removeInvitation(
            @ApiParam(value = "The user's name", required = true) @PathVariable final String username,
            @ApiParam(value = "Type of action: remove or reject", required = true) @RequestParam final String action
    ) {
        log.info("Called with username {}, action {}", username, action);

        this.validUsername(username);

        // Get an authorized user
        final User fromUser = this.authorizationService.getUser();
        // Get the user by username
        final User toUser = this.userSearchService.getUserByUsername(username);

        if (action.equals("remove")) {
            // Delete the invitation
            this.invitationPersistenceService.deleteInvitation(fromUser.getId(), toUser.getId());
        } else /* if action.equals("reject") */ {
            // Delete the invitation
            this.invitationPersistenceService.deleteInvitation(toUser.getId(), fromUser.getId());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(true);
    }

    @ApiOperation(value = "Get the relationship status between users")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No user found"),
            @ApiResponse(code = 403, message = "Forbidden command")
    })
    @GetMapping("/status/{username}")
    public
    ResponseEntity<RelationshipStatus> getStatus(
            @ApiParam(value = "The user's name", required = true) @PathVariable final String username
    ) {
        log.info("Called with username {}", username);

        this.validUsername(username);

        final RelationshipStatus status;
        // Get an authorized user
        final User fromUser = this.authorizationService.getUser();
        // Get the user by username
        final User toUser = this.userSearchService.getUserByUsername(username);

        if (this.friendshipSearchService.getFriendshipExists(fromUser.getId(), toUser.getId())) {
            status = RelationshipStatus.FRIEND;
        } else if (this.invitationSearchService.getInvitationExists(fromUser.getId(), toUser.getId())) {
            status = RelationshipStatus.INVITATION_FROM_YOU;
        } else if (this.invitationSearchService.getInvitationExists(toUser.getId(), fromUser.getId())) {
            status = RelationshipStatus.INVITATION_TO_YOU;
        } else {
            status = RelationshipStatus.UNKNOWN;
        }

        return ResponseEntity.ok().body(status);
    }


    /**
     * This method validates whether the username is not the name of the authorized user.
     *
     * @param username The user's name
     * @throws ResourceForbiddenException if the username is the same as the authorised user's name
     */
    private void validUsername(final String username) {
        if(username.equals(this.authorizationService.getUsername()))
            throw new ResourceForbiddenException("Forbidden command");
    }
}
