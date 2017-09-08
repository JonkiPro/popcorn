package com.service.app.service.impl;

import com.service.app.repository.UserRepository;
import com.service.app.entity.User;
import com.service.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("userService")
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public Optional<User> findByActivationToken(String token) {
        return userRepository.findByActivationToken(token);
    }

    @Override
    public Optional<User> findByEmailChangeToken(String token) {
        return userRepository.findByEmailChangeToken(token);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsernameAndEmail(String username, String email) {
        return userRepository.findByUsernameIgnoreCaseAndEmailIgnoreCase(username, email);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAll(int page, int pageSize, Sort sort) {
        return userRepository.findAllByEnabledTrue(new PageRequest(page, pageSize, sort)).getContent();
    }

    @Override
    public List<User> findByUsernameContaining(String username, int page, int pageSize, Sort sort) {
        return userRepository.findByUsernameContainingIgnoreCaseAndEnabledTrue(username, new PageRequest(page, pageSize, sort)).getContent();
    }

    @Override
    public Long countByUsernameContaining(String username) {
        return userRepository.countByUsernameContainingIgnoreCaseAndEnabledTrue(username);
    }

    @Override
    public User findOneByUsername(String username) {
        return userRepository.findOneByUsernameIgnoreCase(username);
    }
}
