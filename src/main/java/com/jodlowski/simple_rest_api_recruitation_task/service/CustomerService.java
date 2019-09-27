package com.jodlowski.simple_rest_api_recruitation_task.service;

import com.jodlowski.simple_rest_api_recruitation_task.model.Customer;
import com.jodlowski.simple_rest_api_recruitation_task.repository.AddressRepository;
import com.jodlowski.simple_rest_api_recruitation_task.repository.CustomerRepository;
import com.jodlowski.simple_rest_api_recruitation_task.util.ApiLogger;
import com.jodlowski.simple_rest_api_recruitation_task.util.exceptions.CustomerAlreadyExistsException;
import com.jodlowski.simple_rest_api_recruitation_task.util.exceptions.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository,
                           AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
    }

    public Customer getCustomerById(Long id) throws CustomerNotFoundException {
        ApiLogger.fetchingCustomer(id);
        Customer customer = this.customerRepository.findById(id)
                .orElseThrow(() -> {
                    ApiLogger.customerNotFound(id);
                    return new CustomerNotFoundException(id);
                });
        ApiLogger.sendingCustomer(id);
        return customer;
    }

    public Customer addCustomer(Customer customer) throws CustomerAlreadyExistsException {
        ApiLogger.checkingCustomerExistence(customer.getName());
        if (this.customerRepository.existsByNameAndAddress_CityAndAddress_StreetAndAddress_ZipCode(
                customer.getName(),
                customer.getAddress().getCity(),
                customer.getAddress().getStreet(),
                customer.getAddress().getZipCode()
        )) {
            ApiLogger.customerAlreadyExists(customer.getName());
            throw new CustomerAlreadyExistsException(customer.getName());
        }
        this.addressRepository.save(customer.getAddress());
        Customer savedCustomer = this.customerRepository.save(customer);
        ApiLogger.customerCreated(customer.getName());
        return savedCustomer;
    }
}
