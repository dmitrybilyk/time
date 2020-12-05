package com.count.time.services.jwt;



import com.count.time.model.User;

import java.util.List;

/**
 * Service interface for class {@link User}.
 *
 * @author Eugene Suleimanov
 * @version 1.0
 */

public interface UserService {

    User register(User user);

    List<User> getAll();

    User findByLogin(String username);

    User findById(Long id);

    void delete(Long id);
}
