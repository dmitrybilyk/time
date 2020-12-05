package com.count.time.repositories.jwt;

import com.count.time.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link User}.
 *
 * @author Eugene Suleimanov
 * @version 1.0
 */

public interface JWTUserRepository extends JpaRepository<User, Long> {
    User findByLogin(String name);
}
