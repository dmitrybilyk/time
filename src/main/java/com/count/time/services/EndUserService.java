package com.count.time.services;

import com.count.time.model.EndUser;

import java.util.List;

public interface EndUserService {
    void save(EndUser newEndUser);

    List<EndUser> getAllEndUsers();
}
