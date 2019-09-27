package com.jodlowski.simple_rest_api_recruitation_task.repository;

import com.jodlowski.simple_rest_api_recruitation_task.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByNameAndAddress_CityAndAddress_StreetAndAddress_ZipCode(
            String name,
            String city,
            String street,
            int zipCode);
}
