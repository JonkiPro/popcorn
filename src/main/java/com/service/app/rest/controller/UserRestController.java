package com.service.app.rest.controller;

import com.service.app.dto.out.UserInfoDTO;
import com.service.app.dto.out.UserProfileDTO;
import com.service.app.entity.User;
import com.service.app.converter.UnidirectionalConverter;
import com.service.app.service.UserService;
import org.jsondoc.core.annotation.*;
import org.jsondoc.core.pojo.ApiStage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@PreAuthorize("permitAll()")
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(name = "User API", description = "Provides a list of methods that retrieve users and their data", group = "User", stage = ApiStage.BETA)
public class UserRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private UnidirectionalConverter<User, UserInfoDTO> converterUserToUserInfoDTO;
    @Autowired
    private UnidirectionalConverter<User, UserProfileDTO> converterUserToUserProfileDTO;

    @ApiMethod(description = "Get the number of users by fragment of username")
    @GetMapping(value = "/getNumberOfUsersByUsername")
    @ResponseStatus(HttpStatus.OK)
    public @ApiResponseObject
    HttpEntity<Long> getNumberOfUsersByUsername(
            @ApiQueryParam(description = "Fragment of username") @RequestParam String username
    ) {
        return ResponseEntity.ok().body(userService.countByUsernameContaining(username));
    }

    @ApiMethod(description = "Get list of users by phrase")
    @GetMapping(value = "/getUsers")
    @ResponseStatus(HttpStatus.OK)
    public @ApiResponseObject
    HttpEntity<List<UserInfoDTO>> getUsers(
            @ApiQueryParam(description = "Search for a phrase") @RequestParam(required = false) String q,
            @ApiQueryParam(description = "Page number") @RequestParam(required = false, defaultValue = "1") int page,
            @ApiQueryParam(description = "Number of items per page") @RequestParam(required = false, defaultValue = "1") int pageSize,
            @ApiQueryParam(description = "Sort field") @RequestParam(required = false, defaultValue = "id") String sort
    ) {
        return Optional
                .ofNullable(q)
                .map(v -> ResponseEntity.ok().body(converterUserToUserInfoDTO.convertAll(userService.findByUsernameContaining(q, page - 1, pageSize, new Sort(Sort.Direction.ASC, sort)))))
                .orElseGet(() ->
                        ResponseEntity.ok().body(converterUserToUserInfoDTO.convertAll(userService.findAll(page - 1, pageSize, new Sort(Sort.Direction.ASC, sort))))
                );
    }

    @ApiMethod(description = "Get user profile by username")
    @ApiErrors(apierrors = { @ApiError(code = "404", description = "No user found") })
    @GetMapping(value = "/getProfile")
    public @ApiResponseObject
    HttpEntity<UserProfileDTO> getProfile(
            @ApiQueryParam(description = "The user's name") @RequestParam String username
    ) {
        return Optional
                .ofNullable(userService.findOneByUsername(username))
                .map(user -> ResponseEntity.ok().body(converterUserToUserProfileDTO.convert(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
