package com.jodlowski.simple_rest_api_recruitation_task.util.exceptions;


public class CustomerAlreadyExistsException extends RuntimeException {

    public CustomerAlreadyExistsException(String name) {
        super(String.format("Unable to create! Customer with name %s and provided address already exists.", name));
    }
}
