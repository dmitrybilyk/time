package com.count.time.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Service {

    public enum TYPE {
        SINGLE, MULTI_HALF, MULTI_FULL
    }
    @Id
    @GeneratedValue
    private int id;
    private String name;

    @ManyToMany
    List<User> endUsers;

    public Service() {
    }

    public Service(String name, List<User> endUsers) {
        this.name = name;
        this.endUsers = endUsers;
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

    public List<User> getEndUsers() {
        return endUsers;
    }

    public void setEndUsers(List<User> endUsers) {
        this.endUsers = endUsers;
    }
}
