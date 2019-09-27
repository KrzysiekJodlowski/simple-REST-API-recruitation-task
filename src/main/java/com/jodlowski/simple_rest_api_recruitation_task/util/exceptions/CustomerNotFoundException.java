package com.jodlowski.simple_rest_api_recruitation_task.util.exceptions;


public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Long id) {
        super(String.format("Customer with id %s has not been found.", id));
    }

}
