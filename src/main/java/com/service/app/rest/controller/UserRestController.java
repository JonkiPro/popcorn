package com.service.app.rest.controller;

import com.service.app.rest.response.UserInfoDTO;
import com.service.app.rest.response.UserProfileDTO;
import com.service.app.entity.User;
import com.service.app.converter.UnidirectionalConverter;
import com.service.app.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@PreAuthorize("permitAll()")
@RequestMapping(value = "/api/v1.0/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "User API", description = "Provides a list of methods that retrieve users and their data")
public class UserRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private UnidirectionalConverter<User, UserInfoDTO> converterUserToUserInfoDTO;
    @Autowired
    private UnidirectionalConverter<User, UserProfileDTO> converterUserToUserProfileDTO;

    @ApiOperation(value = "Get list of users by phrase")
    @GetMapping
    public
    HttpEntity<List<UserInfoDTO>> getUsers(
            @ApiParam(value = "Search for a phrase") @RequestParam(required = false) String q,
            @ApiParam(value = "Page number") @RequestParam(required = false, defaultValue = "1") int page,
            @ApiParam(value = "Number of items per page") @RequestParam(required = false, defaultValue = "1") int pageSize,
            @ApiParam(value = "Sort field") @RequestParam(required = false, defaultValue = "id") String sort
    ) {
        return Optional
                .ofNullable(q)
                .map(v -> ResponseEntity.ok().body(converterUserToUserInfoDTO.convertAll(userService.findByUsernameContaining(q, page - 1, pageSize, new Sort(Sort.Direction.ASC, sort)))))
                .orElseGet(() ->
                        ResponseEntity.ok().body(converterUserToUserInfoDTO.convertAll(userService.findAll(page - 1, pageSize, new Sort(Sort.Direction.ASC, sort))))
                );
    }

    @ApiOperation(value = "Get user profile by username")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No user found") })
    @GetMapping(value = "/account/{username}")
    public
    HttpEntity<UserProfileDTO> getProfile(
            @ApiParam(value = "The user's name", required = true) @PathVariable String username
    ) {
        return Optional
                .ofNullable(userService.findOneByUsername(username))
                .map(user -> ResponseEntity.ok().body(converterUserToUserProfileDTO.convert(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Get the number of users by fragment of username")
    @GetMapping(value = "/number")
    @ResponseStatus(HttpStatus.OK)
    public
    HttpEntity<Long> getNumberOfUsers(
            @ApiParam(value = "Fragment of username", required = true) @RequestParam String username
    ) {
        return ResponseEntity.ok().body(userService.countByUsernameContaining(username));
    }
}
