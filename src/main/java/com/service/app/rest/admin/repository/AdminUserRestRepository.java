package com.service.app.rest.admin.repository;

import com.service.app.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RepositoryRestResource
@PreAuthorize("hasRole('ROLE_ADMIN')")
public interface AdminUserRestRepository extends PagingAndSortingRepository<User, Long> {

    List<User> findAll();

    List<User> findAllByEnabled(boolean enabled);

    List<User> findAllByUsernameContainingIgnoreCase(String username);

    List<User> findAllByUsernameContainingIgnoreCaseAndEnabled(String username, boolean enabled);
}
