package com.count.time.services.impl;

import com.count.time.model.Service;
import com.count.time.repositories.ServiceRepository;
import com.count.time.services.ServiceService;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {
    private ServiceRepository serviceRepository;

    public ServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public void save(Service service) {
        serviceRepository.save(service);
    }

    @Override
    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }
}
