package com.web.web.controller;

import com.common.dto.search.UserSearchResult;
import com.common.exception.ResourceConflictException;
import com.common.exception.ResourceForbiddenException;
import com.core.service.UserSearchService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.web.web.hateoas.assembler.UserResourceAssembler;
import com.web.web.hateoas.assembler.UserSearchResultResourceAssembler;
import com.web.web.hateoas.resource.UserResource;
import com.web.web.hateoas.resource.UserSearchResultResource;
import com.web.web.security.service.AuthorizationService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1.0/users")
@Slf4j
@Api(value = "User API", description = "Provides a list of methods that retrieve users and their data")
public class UserRestController {

    private final UserSearchService userSearchService;
    private final AuthorizationService authorizationService;
    private final UserResourceAssembler userResourceAssembler;
    private final UserSearchResultResourceAssembler userSearchResultResourceAssembler;

    /**
     * Constructor.
     *
     * @param userSearchService The user search service to use
     * @param authorizationService The authorization service to use
     * @param userResourceAssembler Assemble user resources out of users
     * @param userSearchResultResourceAssembler Assemble user search resources out of users
     */
    @Autowired
    public UserRestController(
            final UserSearchService userSearchService,
            final AuthorizationService authorizationService,
            final UserResourceAssembler userResourceAssembler,
            final UserSearchResultResourceAssembler userSearchResultResourceAssembler
    ) {
        this.userSearchService = userSearchService;
        this.authorizationService = authorizationService;
        this.userResourceAssembler = userResourceAssembler;
        this.userSearchResultResourceAssembler = userSearchResultResourceAssembler;
    }

    @ApiOperation(value = "Find users")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    PagedResources<UserSearchResultResource> findUsers(
            @ApiParam(value = "The user's name")
            @RequestParam(value = "q", required = false) final String q,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) final Pageable page,
            final PagedResourcesAssembler<UserSearchResult> assembler
    ) {
        log.info("Called with q {}, page {}", q, page);

        // Build the self link which will be used for the next, previous, etc links
        final Link self = ControllerLinkBuilder
                .linkTo(
                        ControllerLinkBuilder
                                .methodOn(UserRestController.class)
                                .findUsers(
                                        q,
                                        page,
                                        assembler
                                )
                ).withSelfRel();

        return assembler.toResource(this.userSearchService.findUsers(
                q, page
        ), this.userSearchResultResourceAssembler, self);
    }

    @ApiOperation(value = "Get user profile by username")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No user found") })
    @GetMapping(value = "/{username}", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    UserResource getProfile(
            @ApiParam(value = "The user's name", required = true)
            @PathVariable("username") final String username
    ) {
        log.info("Called with username {}", username);

        return this.userResourceAssembler.toResource(this.userSearchService.getUserByUsername(username));
    }

    @ApiOperation(value = "Get user friends")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No user found") })
    @GetMapping(value = "/{username}/friends", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    List<UserResource> getFriends(
            @ApiParam(value = "The user's name", required = true)
            @PathVariable("username") final String username
    ) {
        log.info("Called with username {}", username);

        final String id = this.userSearchService.getUserByUsername(username).getId();

        return this.userSearchService.getFriends(id)
                .stream()
                .map(this.userResourceAssembler::toResource)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Get user friends")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No user found") })
    @GetMapping(value = "/{username}/invitations", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    List<UserResource> getInvitations(
            @ApiParam(value = "The user's name", required = true)
            @PathVariable("username") final String username,
            @ApiParam(value = "True, if invitations sent")
            @RequestParam(value = "outgoing", defaultValue = "0", required = false) final Boolean outgoing
    ) {
        log.info("Called with username {}, outgoing {}", username, outgoing);

        if(!username.equalsIgnoreCase(this.authorizationService.getUsername())) {
            throw new ResourceForbiddenException("Forbidden command!");
        }

        final String id = this.authorizationService.getUserId();

        return this.userSearchService.getInvitations(id, outgoing)
                .stream()
                .map(this.userResourceAssembler::toResource)
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Get the relationship status between users")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No user found") })
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/{username}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    JsonNode getUserFriendStatus(
            @ApiParam(value = "The user's name", required = true)
            @PathVariable("username") final String username
    ) {
        log.info("Called with username {}", username);

        final String authId = this.authorizationService.getUserId();
        final String otherId = this.userSearchService.getUserByUsername(username).getId();

        if(authId.equals(otherId)) {
            throw new ResourceConflictException("Conflict IDs");
        }

        final JsonNodeFactory factory = JsonNodeFactory.instance;
        return factory
                .objectNode()
                .set("status", factory.textNode(this.userSearchService.getUserFriendStatus(authId, otherId).toString()));
    }
}
