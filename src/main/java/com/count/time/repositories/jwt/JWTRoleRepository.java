package com.count.time.repositories.jwt;

import com.count.time.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link Role}.
 *
 * @author Eugene Suleimanov
 * @version 1.0
 */

public interface JWTRoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
