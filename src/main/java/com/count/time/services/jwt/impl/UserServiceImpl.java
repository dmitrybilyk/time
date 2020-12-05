package com.count.time.services.jwt.impl;

import com.count.time.model.Role;
import com.count.time.model.User;
import com.count.time.repositories.jwt.JWTRoleRepository;
import com.count.time.repositories.jwt.JWTUserRepository;
import com.count.time.services.jwt.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link UserService} interface.
 * Wrapper for {@link JWTUserRepository} + business logic.
 *
 * @author Eugene Suleimanov
 * @version 1.0
 */

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final JWTUserRepository JWTUserRepository;
    private final JWTRoleRepository JWTRoleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(JWTUserRepository JWTUserRepository, JWTRoleRepository JWTRoleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.JWTUserRepository = JWTUserRepository;
        this.JWTRoleRepository = JWTRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        Role roleUser = JWTRoleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);

        User registeredUser = JWTUserRepository.save(user);

        log.info("IN register - user: {} successfully registered", registeredUser);

        return registeredUser;
    }

    @Override
    public List<User> getAll() {
        List<User> result = JWTUserRepository.findAll();
        log.info("IN getAll - {} users found", result.size());
        return result;
    }

    @Override
    public User findByLogin(String username) {
        User result = JWTUserRepository.findByLogin(username);
        log.info("IN findByUsername - user: {} found by username: {}", result, username);
        return result;
    }

    @Override
    public User findById(Long id) {
        User result = JWTUserRepository.findById(id).orElse(null);

        if (result == null) {
            log.warn("IN findById - no user found by id: {}", id);
            return null;
        }

        log.info("IN findById - user: {} found by id: {}", result);
        return result;
    }

    @Override
    public void delete(Long id) {
        JWTUserRepository.deleteById(id);
        log.info("IN delete - user with id: {} successfully deleted");
    }
}
