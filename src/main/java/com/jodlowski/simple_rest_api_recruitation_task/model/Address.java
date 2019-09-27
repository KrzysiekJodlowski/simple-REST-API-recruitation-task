package com.jodlowski.simple_rest_api_recruitation_task.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


@Data
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;
    private String city;
    private String street;
    private int zipCode;

    public Address() {}
    public Address(String city, String street, int zipCode) {
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
    }

}
