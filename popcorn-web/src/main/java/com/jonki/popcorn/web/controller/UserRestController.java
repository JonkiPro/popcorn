package com.jonki.popcorn.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.jonki.popcorn.common.dto.search.UserSearchResult;
import com.jonki.popcorn.common.exception.ResourceConflictException;
import com.jonki.popcorn.common.exception.ResourceForbiddenException;
import com.jonki.popcorn.core.service.AuthorizationService;
import com.jonki.popcorn.core.service.UserSearchService;
import com.jonki.popcorn.web.hateoas.assembler.UserResourceAssembler;
import com.jonki.popcorn.web.hateoas.assembler.UserSearchResultResourceAssembler;
import com.jonki.popcorn.web.hateoas.resource.UserResource;
import com.jonki.popcorn.web.hateoas.resource.UserSearchResultResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST end-point for supporting Users.
 */
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

    /**
     * Get users for given filter criteria.
     *
     * @param q Phrase to search in the user's name (optional)
     * @param page The page to get
     * @param assembler The paged resources assembler to use
     * @return All users matching the criteria
     */
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

    /**
     * Get User for given id.
     *
     * @param username The user's name
     * @return The user
     */
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

    /**
     * Get all the friends (Users) for a given user.
     *
     * @param username The user's name. Not null/empty/blank
     * @return The list of friends
     */
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

    /**
     * Get all the invitations (Users) for a given user.
     *
     * @param username The user's name. Not null/empty/blank
     * @param outgoing True, if invitations sent. False, if invitations received (optional, default false)
     * @return The list of invitations
     */
    @ApiOperation(value = "Get user invitations")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No user found") })
    @PreAuthorize("hasRole('ROLE_USER')")
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

        return this.userSearchService.getInvitations(outgoing)
                .stream()
                .map(this.userResourceAssembler::toResource)
                .collect(Collectors.toList());
    }

    /**
     * Get the status of a relationship with a user with the given name.
     *
     * @param username The user's name. Not null/empty/blank
     * @return The status of a relationship
     */
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
                .set("status", factory.textNode(this.userSearchService.getUserFriendStatus(otherId).toString()));
    }
}
