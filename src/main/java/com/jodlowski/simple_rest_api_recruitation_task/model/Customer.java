package com.jodlowski.simple_rest_api_recruitation_task.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


@Data
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;
    private String name;
    @ManyToOne
    private Address address;

    public Customer() {}
    public Customer(String name, Address address) {
        this.name = name;
        this.address = address;
    }

}