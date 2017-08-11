package com.service.app.rest.controller;

import com.service.app.dto.out.RelationshipStatusDTO;
import com.service.app.entity.Friendship;
import com.service.app.entity.Invitation;
import com.service.app.service.AuthorizationService;
import com.service.app.service.FriendshipService;
import com.service.app.service.InvitationService;
import com.service.app.service.UserService;
import org.jsondoc.core.annotation.*;
import org.jsondoc.core.pojo.ApiStage;
import org.jsondoc.core.pojo.ApiVerb;
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
@Api(name = "User Relation API", description = "Provides a list of methods that manage user relationships", group = "User", stage = ApiStage.BETA)
public class UserRelationRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private InvitationService invitationService;
    @Autowired
    private FriendshipService friendshipService;

    @ApiMethod(description = "Save invitation", verb = ApiVerb.POST)
    @ApiErrors(apierrors = { @ApiError(code = "400", description = "Incorrect username") })
    @PostMapping("/sendInvitation")
    @SuppressWarnings("ConstantConditions")
    public @ApiResponseObject
    HttpEntity<Boolean> sendInvitation(
            @ApiQueryParam(description = "The user's name") @RequestParam String username
    ) {
        try {
            invitationService.saveInvitation(new Invitation(authorizationService.getUserId(), userService.findByUsername(username).get().getId()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(true, HttpStatus.CREATED);
    }

    @ApiMethod(description = "Remove invitation", verb = ApiVerb.DELETE)
    @ApiErrors(apierrors = { @ApiError(code = "404", description = "No invitation found"), @ApiError(code = "400", description = "Incorrect username") })
    @DeleteMapping("/removeInvitation")
    @SuppressWarnings("ConstantConditions")
    public @ApiResponseObject
    HttpEntity<Boolean> removeInvitation(
            @ApiQueryParam(description = "The user's name") @RequestParam String username
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

    @ApiMethod(description = "Reject invitation", verb = ApiVerb.DELETE)
    @ApiErrors(apierrors = { @ApiError(code = "404", description = "No invitation found"), @ApiError(code = "400", description = "Incorrect username") })
    @DeleteMapping("/rejectInvitation")
    @SuppressWarnings("ConstantConditions")
    public @ApiResponseObject
    HttpEntity<Boolean> rejectInvitation(
            @ApiQueryParam(description = "The user's name") @RequestParam String username
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

    @ApiMethod(description = "Save friendship", verb = ApiVerb.POST)
    @ApiErrors(apierrors = { @ApiError(code = "404", description = "No invitation found"), @ApiError(code = "400", description = "Incorrect username") })
    @PostMapping("/addFriend")
    @SuppressWarnings("ConstantConditions")
    public @ApiResponseObject
    HttpEntity<Boolean> saveFriendship(
            @ApiQueryParam(description = "The user's name") @RequestParam String username
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

    @ApiMethod(description = "Remove friendship", verb = ApiVerb.DELETE)
    @ApiErrors(apierrors = { @ApiError(code = "404", description = "No invitation found"), @ApiError(code = "400", description = "Incorrect username") })
    @DeleteMapping("/removeFriend")
    @SuppressWarnings("ConstantConditions")
    public @ApiResponseObject
    HttpEntity<Boolean> removeFriend(
            @ApiQueryParam(description = "The user's name") @RequestParam String username
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

    @ApiMethod(description = "Get the relationship status between users")
    @ApiErrors(apierrors = { @ApiError(code = "400", description = "Incorrect username") })
    @GetMapping("/getStatus")
    @SuppressWarnings("ConstantConditions")
    public @ApiResponseObject
    ResponseEntity<RelationshipStatusDTO> getStatus(
            @ApiQueryParam(description = "The user's name") @RequestParam String username
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
