package com.service.app.service;

import com.service.app.entity.User;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void saveUser(User user);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findByActivationToken(String activationToken);
    Optional<User> findByEmailChangeToken(String emailChangeToken);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);

    Optional<User> findByUsernameAndEmail(String username, String email);

    List<User> findAll();

    List<User> findAll(int page, int pageSize, Sort sort);

    List<User> findByUsernameContaining(String username, int page, int pageSize, Sort sort);

    Long countByUsernameContaining(String username);

    User findOneByUsername(String username);
}
