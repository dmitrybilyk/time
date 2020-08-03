package com.count.time.services;

import com.count.time.model.Service;

import java.util.List;

public interface ServiceService {
    void save(Service service);

    List<Service> getAllServices();
}
