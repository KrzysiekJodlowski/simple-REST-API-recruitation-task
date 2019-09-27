package com.jodlowski.simple_rest_api_recruitation_task;

import com.jodlowski.simple_rest_api_recruitation_task.model.Address;
import com.jodlowski.simple_rest_api_recruitation_task.model.Customer;
import com.jodlowski.simple_rest_api_recruitation_task.repository.AddressRepository;
import com.jodlowski.simple_rest_api_recruitation_task.repository.CustomerRepository;
import com.jodlowski.simple_rest_api_recruitation_task.service.CustomerService;
import com.jodlowski.simple_rest_api_recruitation_task.util.exceptions.CustomerAlreadyExistsException;
import com.jodlowski.simple_rest_api_recruitation_task.util.exceptions.CustomerNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
public class CustomerServiceTests {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer defaultCustomer;

    @Before
    public void mockServiceAndAssembler() {
        Address customerAddress = new Address("New York", "Main street", 1234567);
        customerAddress.setId(1L);
        this.defaultCustomer = new Customer("Alex", customerAddress);
        this.defaultCustomer.setId(1L);

        when(this.customerRepository.save(any(Customer.class))).thenReturn(this.defaultCustomer);
        when(this.addressRepository.save(any(Address.class))).thenReturn(customerAddress);
        when(this.customerRepository.findById(1L)).thenReturn(java.util.Optional.of(this.defaultCustomer));
    }

    @Test
    public void testReturnCustomerWhenAddingNewCustomer() {
        this.setCustomerRepositoryReturnValueForCustomerExistingCheck(false);
        Customer savedCustomer = customerService.addCustomer(this.defaultCustomer);
        assertThat(savedCustomer.getName()).isSameAs(this.defaultCustomer.getName());
        assertThat(savedCustomer.getAddress()).isSameAs(this.defaultCustomer.getAddress());
    }

    @Test
    public void testReturnCustomerWhenGettingExistingCustomer() {
        Customer savedCustomer = customerService.getCustomerById(this.defaultCustomer.getId());
        assertThat(savedCustomer.getName()).isSameAs(this.defaultCustomer.getName());
        assertThat(savedCustomer.getAddress()).isSameAs(this.defaultCustomer.getAddress());
    }

    @Test
    public void testThrowsCustomerAlreadyExistsExceptionWhenAddingExistingCustomer() {
        this.setCustomerRepositoryReturnValueForCustomerExistingCheck(true);
        assertThrows(CustomerAlreadyExistsException.class, () -> customerService.addCustomer(this.defaultCustomer),
                "Unable to create! Customer with name Alex and provided address already exists.");
    }

    @Test
    public void testThrowsCustomerNotFoundExceptionWhenGettingNonExistingCustomer() {
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(2L),
                "Customer with id 2 has not been found.");
    }

    private void setCustomerRepositoryReturnValueForCustomerExistingCheck(boolean doesCustomerExist) {
        when(this.customerRepository.existsByNameAndAddress_CityAndAddress_StreetAndAddress_ZipCode(
                any(String.class),
                any(String.class),
                any(String.class),
                any(int.class)
        )).thenReturn(doesCustomerExist);
    }
}
