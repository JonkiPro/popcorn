package com.service.app.rest.admin.controller;

import com.service.app.entity.User;
import com.service.app.rest.admin.repository.AdminUserRestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@RepositoryRestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping(value = "/admin/users")
public class AdminUserRestController {

    @Autowired
    private AdminUserRestRepository  userRepository;

    @GetMapping
    public @ResponseBody
    List<User> getUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String enabled
    ) {
        return Optional
                .ofNullable(username)
                .map(varUsername ->
                        Optional
                                .ofNullable(enabled)
                                .map(varEnabled ->
                                        userRepository
                                                .findAllByUsernameContainingIgnoreCaseAndEnabled(varUsername, Boolean.valueOf(varEnabled))
                                )
                                .orElseGet(() -> userRepository.findAllByUsernameContainingIgnoreCase(varUsername))
                    )
                .orElseGet(() ->
                        Optional
                                .ofNullable(enabled)
                                .map(varEnabled ->
                                        userRepository.findAllByEnabled(Boolean.valueOf(varEnabled))
                                )
                                .orElseGet(() -> userRepository.findAll())
                );
    }
}
