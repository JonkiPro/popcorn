package com.service.app.rest.controller;

import com.service.app.entity.User;
import com.service.app.exception.ResourceConflictException;
import com.service.app.exception.ResourceForbiddenException;
import com.service.app.exception.ResourceNotFoundException;
import com.service.app.rest.response.RelationshipStatus;
import com.service.app.rest.response.RelationshipStatusDTO;
import com.service.app.entity.Friendship;
import com.service.app.entity.Invitation;
import com.service.app.service.AuthorizationService;
import com.service.app.service.FriendshipService;
import com.service.app.service.InvitationService;
import com.service.app.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/api/v1.0/relations")
@Api(value = "User Relation API", description = "Provides a list of methods that manage user relationships")
public class UserRelationRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private InvitationService invitationService;
    @Autowired
    private FriendshipService friendshipService;

    @ApiOperation(value = "Save friendship")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No invitation or user found"),
            @ApiResponse(code = 403, message = "Authorised user"),
            @ApiResponse(code = 409, message = "There is a friendship")
    })
    @PostMapping("/friends/{username}")
    public
    HttpEntity<Boolean> saveFriendship(
            @ApiParam(value = "The user's name", required = true) @PathVariable String username
    ) {
        this.validUsername(username);

        // Get authorization User
        User fromUser = authorizationService.getUser();
        // Get User by username
        User toUser = userService.findByUsername(username)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        this.validFriendship(fromUser, toUser);

        return invitationService.findInvitation(toUser, fromUser)
                .map(invitation -> {
                    // Remove invitation
                    invitationService.removeInvitation(invitation);
                    // Save friendship
                    friendshipService.saveFriendship(new Friendship(fromUser, toUser));

                    return ResponseEntity.status(HttpStatus.CREATED).body(true);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Remove friendship")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No invitation or user found"),
            @ApiResponse(code = 403, message = "Authorised user")
    })
    @DeleteMapping("/friends/{username}")
    public
    HttpEntity<Boolean> removeFriendship(
            @ApiParam(value = "The user's name", required = true) @PathVariable String username
    ) {
        this.validUsername(username);

        // Get authorization User
        User fromUser = authorizationService.getUser();
        // Get User by username
        User toUser = userService.findByUsername(username)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return friendshipService.findFriendship(fromUser, toUser)
                .map(friendship -> {
                    // Remove friendship
                    friendshipService.removeFriendship(friendship);

                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(true);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Save invitation")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No user found"),
            @ApiResponse(code = 403, message = "Authorised user"),
            @ApiResponse(code = 409, message = "There is an invitation or friendship")
    })
    @PostMapping("/invitations/{username}")
    public
    HttpEntity<Boolean> sendInvitation(
            @ApiParam(value = "The user's name", required = true) @PathVariable String username
    ) {
        this.validUsername(username);

        // Get authorization User
        User fromUser = authorizationService.getUser();
        // Get User by username
        User toUser = userService.findByUsername(username)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        this.validInvitation(fromUser, toUser);
        this.validFriendship(fromUser, toUser);

        // Save invitation
        invitationService.saveInvitation(new Invitation(fromUser, toUser));

        return ResponseEntity.status(HttpStatus.CREATED).body(true);
    }

    @ApiOperation(value = "Remove/reject invitation")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No invitation or user found"),
            @ApiResponse(code = 403, message = "Authorised user")
    })
    @DeleteMapping("/invitations/{username}")
    public
    HttpEntity<Boolean> removeInvitation(
            @ApiParam(value = "The user's name", required = true) @PathVariable String username,
            @ApiParam(value = "Type of action: remove or reject", required = true) @RequestParam String action
    ) {
        this.validUsername(username);

        // Get authorization User
        User fromUser = authorizationService.getUser();
        // Get User by username
        User toUser = userService.findByUsername(username)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (action.equals("remove")) {
            return invitationService.findInvitation(fromUser, toUser)
                    .map(invitation -> {
                        // Remove invitation
                        invitationService.removeInvitation(invitation);

                        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(true);
                    }).orElseGet(() -> ResponseEntity.notFound().build());
        } else /* if action.equals("reject") */ {
            return invitationService.findInvitation(toUser, fromUser)
                    .map(invitation -> {
                        // Remove invitation
                        invitationService.removeInvitation(invitation);

                        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(true);
                    }).orElseGet(() -> ResponseEntity.notFound().build());
        }
    }

    @ApiOperation(value = "Get the relationship status between users")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No user found"),
            @ApiResponse(code = 403, message = "Authorised user")
    })
    @GetMapping("/status/{username}")
    public
    ResponseEntity<RelationshipStatusDTO> getStatus(
            @ApiParam(value = "The user's name", required = true) @PathVariable String username
    ) {
        this.validUsername(username);

        String status;
        // Get authorization User
        User fromUser = authorizationService.getUser();
        // Get User by username
        User toUser = userService.findByUsername(username)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (friendshipService.existsFriendship(fromUser, toUser)) {
            status = RelationshipStatus.FRIEND.toString();
        } else if (invitationService.existsInvitation(fromUser, toUser)) {
            status = RelationshipStatus.INVITATION_FROM_YOU.toString();
        } else if (invitationService.existsInvitation(toUser, fromUser)) {
            status = RelationshipStatus.INVITATION_TO_YOU.toString();
        } else {
            status = RelationshipStatus.UNKNOWN.toString();
        }

        return ResponseEntity.ok().body(new RelationshipStatusDTO(status));
    }


    /**
     * This method validates whether the username is not the name of the authorised user.
     * @param username The user's name.
     * @throws ResourceForbiddenException if the username is the same as the authorised user's name
     */
    private void validUsername(String username) {
        if(username.equals(authorizationService.getUsername()))
            throw new ResourceForbiddenException("You cannot provide an authorised user name");
    }

    /**
     * This method validates whether an invitation already exists.
     * @param fromUser The user's.
     * @param toUser The user's.
     * @throws ResourceConflictException if there is a conflict with the invitation
     */
    private void validInvitation(User fromUser, User toUser) {
        if(invitationService.findInvitation(fromUser, toUser).isPresent())
            throw new ResourceConflictException("The invitation has already been sent to this user");
        if(invitationService.findInvitation(toUser, fromUser).isPresent())
            throw new ResourceConflictException("This user has already sent you an invitation");
    }

    /**
     * This method validates whether an friendship already exists.
     * @param fromUser The user's.
     * @param toUser The user's.
     * @throws ResourceConflictException if there is a conflict with the friendship
     */
    private void validFriendship(User fromUser, User toUser) {
        if(friendshipService.findFriendship(fromUser, toUser).isPresent()
                || friendshipService.findFriendship(toUser, fromUser).isPresent())
            throw new ResourceConflictException("Users are friends");
    }
}
