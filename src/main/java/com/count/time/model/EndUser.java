package com.count.time.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * The user who ues services of the {@link Customer}
 */
@Entity
public class EndUser {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private long serviceDuration;
    private String serviceDescription;

    @ManyToOne
    private Customer customer;

    public EndUser() {
    }

    public EndUser(String name, long serviceDuration, String serviceDescription, Customer customer) {
        this.name = name;
        this.serviceDuration = serviceDuration;
        this.serviceDescription = serviceDescription;
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getServiceDuration() {
        return serviceDuration;
    }

    public void setServiceDuration(long serviceDuration) {
        this.serviceDuration = serviceDuration;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
