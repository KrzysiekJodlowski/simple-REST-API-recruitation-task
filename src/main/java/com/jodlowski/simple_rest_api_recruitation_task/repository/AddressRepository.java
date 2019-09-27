package com.jodlowski.simple_rest_api_recruitation_task.repository;

import com.jodlowski.simple_rest_api_recruitation_task.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findAddressByCityAndStreetAndZipCode(String city, String street, int zipCode);
}
