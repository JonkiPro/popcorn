package com.service.app.rest.controller;

import com.service.app.dto.out.RelationshipStatusDTO;
import com.service.app.entity.Friendship;
import com.service.app.entity.Invitation;
import com.service.app.service.AuthorizationService;
import com.service.app.service.FriendshipService;
import com.service.app.service.InvitationService;
import com.service.app.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/relations")
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

    @ApiOperation(value = "Save invitation", code = 201)
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Incorrect username") })
    @PostMapping("/sendInvitation")
    @SuppressWarnings("ConstantConditions")
    public
    HttpEntity<Boolean> sendInvitation(
            @ApiParam(value = "The user's name", required = true) @RequestParam String username
    ) {
        try {
            invitationService.saveInvitation(new Invitation(authorizationService.getUserId(), userService.findByUsername(username).get().getId()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(true, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Remove invitation")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No invitation found"),
            @ApiResponse(code = 400, message = "Incorrect username")
    })
    @DeleteMapping("/removeInvitation")
    @SuppressWarnings("ConstantConditions")
    public
    HttpEntity<Boolean> removeInvitation(
            @ApiParam(value = "The user's name", required = true) @RequestParam String username
    ) {
        try {
            invitationService.removeInvitation(invitationService.findInvitation(authorizationService.getUserId(), userService.findByUsername(username).get().getId()));
        } catch (InvalidDataAccessApiUsageException | IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(true);
    }

    @ApiOperation(value = "Reject invitation")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No invitation found"),
            @ApiResponse(code = 400, message = "Incorrect username")
    })
    @DeleteMapping("/rejectInvitation")
    @SuppressWarnings("ConstantConditions")
    public
    HttpEntity<Boolean> rejectInvitation(
            @ApiParam(value = "The user's name", required = true) @RequestParam String username
    ) {
        try {
            invitationService.removeInvitation(invitationService.findInvitation(userService.findByUsername(username).get().getId(), authorizationService.getUserId()));
        } catch (InvalidDataAccessApiUsageException | IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(true);
    }

    @ApiOperation(value = "Save friendship")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No invitation found"),
            @ApiResponse(code = 400, message = "Incorrect username")
    })
    @PostMapping("/addFriend")
    @SuppressWarnings("ConstantConditions")
    public
    HttpEntity<Boolean> saveFriendship(
            @ApiParam(value = "The user's name", required = true) @RequestParam String username
    ) {
        Long fromId = authorizationService.getUserId();
        try {
            Long toId = userService.findByUsername(username).get().getId();

            invitationService.removeInvitation(invitationService.findInvitation(toId, fromId));

            friendshipService.saveFriendship(new Friendship(fromId, toId));
            friendshipService.saveFriendship(new Friendship(toId, fromId));
        } catch (InvalidDataAccessApiUsageException | IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(true, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Remove friendship")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No invitation found"),
            @ApiResponse(code = 400, message = "Incorrect username")
    })
    @DeleteMapping("/removeFriend")
    @SuppressWarnings("ConstantConditions")
    public
    HttpEntity<Boolean> removeFriend(
            @ApiParam(value = "The user's name", required = true) @RequestParam String username
    ) {
        Long fromId = authorizationService.getUserId();
        try {
            Long toId = userService.findByUsername(username).get().getId();

            friendshipService.removeFriendship(friendshipService.findFriendship(fromId, toId));
            friendshipService.removeFriendship(friendshipService.findFriendship(toId, fromId));
        } catch (InvalidDataAccessApiUsageException | IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(true);
    }

    @ApiOperation(value = "Get the relationship status between users")
    @ApiResponses(value = { @ApiResponse(code = 400, message = "Incorrect username") })
    @GetMapping("/getStatus")
    @SuppressWarnings("ConstantConditions")
    public
    ResponseEntity<RelationshipStatusDTO> getStatus(
            @ApiParam(value = "The user's name", required = true) @RequestParam String username
    ) {
        String status;
        Long fromId = authorizationService.getUserId();
        Long toId;

        try {
            toId = userService.findByUsername(username).get().getId();
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }

        if (friendshipService.existsFriendship(fromId, toId)) {
            status = "isFriend";
        } else if (invitationService.existsInvitation(fromId, toId)) {
            status = "existsInvitation";
        } else if (invitationService.existsInvitation(toId, fromId)) {
            status = "existsInvitationToYou";
        } else {
            status = "unknown";
        }

        return ResponseEntity.ok().body(new RelationshipStatusDTO(status));
    }
}
