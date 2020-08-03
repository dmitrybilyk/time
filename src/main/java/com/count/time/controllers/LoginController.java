package com.count.time.controllers;

import com.count.time.model.Role;
import com.count.time.model.User;
import com.count.time.repositories.RoleRepository;
import com.count.time.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@RestController
public class LoginController {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public LoginController(PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/login")
    public ModelAndView login() {
        addUsersAndRolesIfNeeded();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }


    private void addUsersAndRolesIfNeeded() {
        Role adminRole = createRoleIfNotFound("ADMIN");
        Role userRole = createRoleIfNotFound("USER");
        User admin = userRepository.findByLogin("admin");
        if (admin == null) {
            admin = new User();
            admin.setLogin("admin");
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setEmail("admin@test.com");
            admin.setEnabled(true);
            admin.setRoles(Collections.singletonList(adminRole));
            userRepository.save(admin);
        }
        User user = userRepository.findByLogin("user");
        if (user == null) {
            user = new User();
            user.setLogin("user");
            user.setFirstName("user");
            user.setLastName("user");
            user.setPassword(passwordEncoder.encode("user"));
            user.setEmail("user@test.com");
            user.setRoles(Collections.singletonList(userRole));
            user.setEnabled(true);
            userRepository.save(user);
        }
        User user2 = userRepository.findByLogin("user2");
        if (user2 == null) {
            user2 = new User();
            user2.setLogin("user2");
            user2.setFirstName("user2");
            user2.setLastName("user2");
            user2.setPassword(passwordEncoder.encode("user2"));
            user2.setEmail("user2@test.com");
            user2.setRoles(Collections.singletonList(userRole));
            user2.setEnabled(true);
            userRepository.save(user2);
        }
    }

    @Transactional
    Role createRoleIfNotFound(
            String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            roleRepository.save(role);
        }
        return role;
    }

}
