package com.jodlowski.simple_rest_api_recruitation_task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jodlowski.simple_rest_api_recruitation_task.controller.CustomerController;
import com.jodlowski.simple_rest_api_recruitation_task.model.Address;
import com.jodlowski.simple_rest_api_recruitation_task.model.Customer;
import com.jodlowski.simple_rest_api_recruitation_task.service.CustomerService;
import com.jodlowski.simple_rest_api_recruitation_task.util.CustomerResourceAssembler;
import com.jodlowski.simple_rest_api_recruitation_task.util.exceptions.CustomerAlreadyExistsException;
import com.jodlowski.simple_rest_api_recruitation_task.util.exceptions.CustomerNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private CustomerResourceAssembler customerResourceAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    private Address defaultCustomerAddress;
    private Customer defaultCustomer;


    @Before
    public void mockServiceAndAssembler() {
        this.defaultCustomerAddress = new Address("New York", "Main street", 1234567);
        this.defaultCustomerAddress.setId(1L);
        this.defaultCustomer = new Customer("Alex", this.defaultCustomerAddress);
        this.defaultCustomer.setId(1L);
        Resource<Customer> customerResource = new Resource<>(this.defaultCustomer,
                linkTo(methodOn(CustomerController.class)
                        .findById(this.defaultCustomer.getId()))
                        .withSelfRel());

        when(this.customerService.addCustomer(any(Customer.class))).thenReturn(this.defaultCustomer);
        when(this.customerService.getCustomerById(1L)).thenReturn(this.defaultCustomer);
        when(this.customerResourceAssembler.toResource(any(Customer.class))).thenReturn(customerResource);
    }

    @Test
    public void testResponseWhenPostingNewCustomer() throws Exception {
        mockMvc.perform(post("/api/v1/customers/")
                .content(this.objectMapper.writeValueAsString(this.defaultCustomer))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(this.defaultCustomer.getName()))
                .andExpect(jsonPath("$.address").value(this.defaultCustomerAddress));
    }

    @Test
    public void testResponseWhenGettingExistingCustomer() throws Exception {
        mockMvc.perform(get("/api/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(this.defaultCustomer.getName()))
                .andExpect(jsonPath("$.address").value(this.defaultCustomerAddress));
    }

    @Test
    public void testErrorResponseWhenPostingExistingCustomer() throws Exception {
        when(this.customerService.addCustomer(this.defaultCustomer))
                 .thenThrow(new CustomerAlreadyExistsException(this.defaultCustomer.getName()));

        mockMvc.perform(post("/api/v1/customers/")
                .content(this.objectMapper.writeValueAsString(this.defaultCustomer))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value(
                        "Unable to create! Customer with name Alex and provided address already exists."));
    }

    @Test
    public void testErrorResponseWhenGettingNonExistingCustomer() throws Exception {
        when(this.customerService.getCustomerById(1L)).thenThrow(new CustomerNotFoundException(1L));

        mockMvc.perform(get("/api/v1/customers/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value(
                        "Customer with id 1 has not been found."));
    }
}
