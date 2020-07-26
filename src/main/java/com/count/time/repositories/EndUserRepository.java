package com.count.time.repositories;

import com.count.time.model.EndUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EndUserRepository extends JpaRepository<EndUser, Integer> {
}
