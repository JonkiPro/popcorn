package com.web.web.controller;

import com.core.jpa.service.UserSearchService;
import com.web.web.hateoas.assembler.UserResourceAssembler;
import com.web.web.hateoas.resource.UserResource;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("permitAll()")
@RequestMapping(value = "/api/v1.0/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Api(value = "UserEntity API", description = "Provides a list of methods that retrieve users and their data")
public class UserRestController {

    private final UserSearchService userSearchService;
    private final UserResourceAssembler userResourceAssembler;

    /**
     * Constructor.
     *
     * @param userSearchService The user search service to use
     * @param userResourceAssembler Assemble user resources out of users
     */
    @Autowired
    public UserRestController(
            final UserSearchService userSearchService,
            final UserResourceAssembler userResourceAssembler
    ) {
        this.userSearchService = userSearchService;
        this.userResourceAssembler = userResourceAssembler;
    }

    @ApiOperation(value = "Get list of users by phrase")
    @GetMapping
    public
    HttpEntity<List<UserResource>> getUsers(
            @ApiParam(value = "Search for a phrase") @RequestParam(required = false) final String q,
            @ApiParam(value = "Page number") @RequestParam(required = false, defaultValue = "1") final int page,
            @ApiParam(value = "Number of items per page") @RequestParam(required = false, defaultValue = "1") final int pageSize,
            @ApiParam(value = "Sort field") @RequestParam(required = false, defaultValue = "id") final String sort
    ) {
        log.info("Called with q {}, page {}, pageSize {}, sort {}", q, page, pageSize, sort);

        return ResponseEntity.ok().body(Optional.ofNullable(q)
                .map(v ->
                        this.userSearchService
                                .getAllUsersByUsername(q, page - 1, pageSize, new Sort(Sort.Direction.ASC, sort))
                                .stream()
                                .map(this.userResourceAssembler::toResource)
                                .collect(Collectors.toList())
                )
                .orElseGet(() ->
                        this.userSearchService
                                .getAllUsers(page - 1, pageSize, new Sort(Sort.Direction.ASC, sort))
                                .stream()
                                .map(this.userResourceAssembler::toResource)
                                .collect(Collectors.toList())
                ));
    }

    @ApiOperation(value = "Get user profile by username")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No user found") })
    @GetMapping(value = "/account/{username}")
    public
    HttpEntity<UserResource> getProfile(
            @ApiParam(value = "The user's name", required = true) @PathVariable final String username
    ) {
        log.info("Called with username {}", username);

        return ResponseEntity.ok()
                .body(this.userResourceAssembler.toResource(this.userSearchService.getUserByUsername(username)));
    }

    @ApiOperation(value = "Get the number of users by fragment of username")
    @GetMapping(value = "/number")
    public
    HttpEntity<Long> getNumberOfUsers(
            @ApiParam(value = "Fragment of username", required = true) @RequestParam final String username
    ) {
        log.info("Called with username {}", username);

        return ResponseEntity.ok().body(this.userSearchService.getUserCountByUsername(username));
    }
}
