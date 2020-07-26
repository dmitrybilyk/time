package com.count.time.services;

import com.count.time.model.Customer;

import java.util.List;

public interface CustomerService {
    void save(Customer newCustomer);

    List<Customer> getAllCustomers();
}
