package com.count.time.services.impl;

import com.count.time.model.EndUser;
import com.count.time.repositories.EndUserRepository;
import com.count.time.services.EndUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EndUserServiceImpl implements EndUserService {
    private EndUserRepository endUserRepository;

    public EndUserServiceImpl(EndUserRepository endUserRepository) {
        this.endUserRepository = endUserRepository;
    }

    public void save(EndUser newEndUser) {
        endUserRepository.save(newEndUser);
    }

    public List<EndUser> getAllEndUsers() {
        return endUserRepository.findAll();
    }
}

