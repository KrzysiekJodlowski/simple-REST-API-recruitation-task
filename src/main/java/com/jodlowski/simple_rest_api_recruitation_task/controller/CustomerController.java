package com.jodlowski.simple_rest_api_recruitation_task.controller;

import com.jodlowski.simple_rest_api_recruitation_task.model.Customer;
import com.jodlowski.simple_rest_api_recruitation_task.service.CustomerService;
import com.jodlowski.simple_rest_api_recruitation_task.util.CustomerResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;


@RestController
@RequestMapping("/api/v1/customers")
public
class CustomerController {

    private final CustomerService customerService;
    private final CustomerResourceAssembler customerResourceAssembler;

    @Autowired
    public CustomerController(CustomerService customerService,
                              CustomerResourceAssembler customerResourceAssembler) {
        this.customerService = customerService;
        this.customerResourceAssembler = customerResourceAssembler;
    }

    @GetMapping("/{id}")
    public Resource<Customer> findById(@PathVariable("id") Long id) {
        Customer customer = this.customerService.getCustomerById(id);
        return this.customerResourceAssembler.toResource(customer);
    }

    @PostMapping
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer) throws URISyntaxException {
        Customer addedCustomer = this.customerService.addCustomer(customer);
        Resource<Customer> resource = this.customerResourceAssembler.toResource(addedCustomer);
        return ResponseEntity
                .created(new URI(resource.getId().expand().getHref()))
                .body(resource);
    }
}
