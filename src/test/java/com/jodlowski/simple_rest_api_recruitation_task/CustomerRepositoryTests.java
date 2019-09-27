package com.jodlowski.simple_rest_api_recruitation_task;

import com.jodlowski.simple_rest_api_recruitation_task.model.Address;
import com.jodlowski.simple_rest_api_recruitation_task.model.Customer;
import com.jodlowski.simple_rest_api_recruitation_task.repository.AddressRepository;
import com.jodlowski.simple_rest_api_recruitation_task.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testSavingNewCustomer() {
        Address customerAddress = new Address("New York", "Main street", 1234567);
        this.entityManager.persistAndFlush(customerAddress);
        Customer customer = new Customer("Alex",customerAddress);
        Customer persistedCustomer = this.entityManager.persistAndFlush(customer);

        assertThat(persistedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(persistedCustomer.getAddress()).isEqualTo(customer.getAddress());
    }

    @Test
    public void testFindingExistingCustomerById() {
        Address customerAddress = new Address("Wrocław", "Ślusarska", 32146);
        Address persistedAddress = this.entityManager.persistAndFlush(customerAddress);
        Customer customer = new Customer("Jan", customerAddress);
        Customer persistedCustomer = this.entityManager.persistAndFlush(customer);

        Customer customerFound = new Customer();
        if(this.customerRepository.findById(persistedCustomer.getId()).isPresent()) {
            customerFound = this.customerRepository.findById(persistedCustomer.getId()).get();
        }

        assertThat(customerFound.getName()).isEqualTo(persistedCustomer.getName());
        assertThat(customerFound.getAddress()).isEqualTo(persistedAddress);
    }

    @Test
    public void testExistingCustomerExistenceCheck() {
        Address customerAddress = new Address("Helsinki", "Arkadiankatu", 2016);
        this.entityManager.persistAndFlush(customerAddress);
        Customer customer = new Customer("Inari", customerAddress);
        Customer persistedCustomer = this.entityManager.persistAndFlush(customer);

        boolean customerExists = this.customerRepository.existsByNameAndAddress_CityAndAddress_StreetAndAddress_ZipCode(
                persistedCustomer.getName(),
                persistedCustomer.getAddress().getCity(),
                persistedCustomer.getAddress().getStreet(),
                persistedCustomer.getAddress().getZipCode()
        );

        assertTrue(customerExists);
    }

    @Test
    public void testNotExistingCustomerExistenceCheck() {
        boolean customerExists = this.customerRepository.existsByNameAndAddress_CityAndAddress_StreetAndAddress_ZipCode(
                "Jolanta",
                "Warszawa",
                "Mazowiecka",
                10101
        );

        assertFalse(customerExists);
    }

    @Test
    public void testEmptyResponseWhenFindingNotExistingCustomer() {
        assertFalse(this.customerRepository.findById(100L).isPresent());
    }

}