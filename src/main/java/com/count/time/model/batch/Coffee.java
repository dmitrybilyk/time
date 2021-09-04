package com.count.time.model.batch;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class Coffee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private String origin;
    private String characteristics;

    public Coffee(String brand, String origin, String characteristics) {
        this.brand = brand;
        this.origin = origin;
        this.characteristics = characteristics;
    }

    // getters and setters
}