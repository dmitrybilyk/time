package com.count.time.repositories;

import com.count.time.model.Role;
import com.count.time.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    User findByLogin(String login);

    List<User> findAllByRolesContaining(Role role);

}
