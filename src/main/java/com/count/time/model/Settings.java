package com.count.time.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int visits;
}
