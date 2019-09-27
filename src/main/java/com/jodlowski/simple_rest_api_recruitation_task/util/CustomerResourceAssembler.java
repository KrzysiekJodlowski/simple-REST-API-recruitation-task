package com.jodlowski.simple_rest_api_recruitation_task.util;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.jodlowski.simple_rest_api_recruitation_task.controller.CustomerController;
import com.jodlowski.simple_rest_api_recruitation_task.model.Customer;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;


@Component
public class CustomerResourceAssembler implements ResourceAssembler<Customer, Resource<Customer>> {

    @Override
    public Resource<Customer> toResource(Customer customer) {

        return new Resource<>(customer,
                linkTo(methodOn(CustomerController.class)
                        .findById(customer.getId()))
                        .withSelfRel());
    }
}