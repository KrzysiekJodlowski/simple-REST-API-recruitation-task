package com.jodlowski.simple_rest_api_recruitation_task;

import com.jodlowski.simple_rest_api_recruitation_task.model.Address;
import com.jodlowski.simple_rest_api_recruitation_task.model.Customer;
import com.jodlowski.simple_rest_api_recruitation_task.util.ApiError;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;
    private final MediaType jsonMimeType = MediaType.APPLICATION_JSON_UTF8;
    private Address defaultCustomerAddress;

    @Before
    public void setDefaultUser() {
        this.defaultCustomerAddress = new Address("Cracow", "Grodzka", 1234567);
        Customer defaultCustomer = new Customer("Zbigniew", this.defaultCustomerAddress);
        this.restTemplate.postForEntity("/api/v1/customers/", defaultCustomer, Customer.class);
    }

    @Test
    public void testStatus200WhenCustomerExists() {
        ResponseEntity<Customer> response = this.getExistingCustomer();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testStatus404WhenCustomerDoesNotExists() {
        ResponseEntity<ApiError> response = this.getErrorBecauseOfNonExistingCustomer();
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testStatus201WhenPostedNonExistingCustomer() {
        Address customerAddress = new Address("Warsaw", "Marszałkowska", 00110);
        Customer customer = new Customer("Mateusz", customerAddress);

        ResponseEntity<Customer> response = this.restTemplate
                .postForEntity("/api/v1/customers/", customer, Customer.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void testStatus409WhenPostedAlreadyExistingCustomer() {
        Address customerAddress = new Address("Cracow", "Grodzka", 1234567);
        Customer customer = new Customer("Zbigniew", customerAddress);

        ResponseEntity<ApiError> response = this.restTemplate
                .postForEntity("/api/v1/customers/", customer, ApiError.class);
        assertThat(response.getStatusCodeValue()).isEqualTo(409);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void testResponseContentTypeIsJsonWhenQueryIsValid() {
        Address customerAddress = new Address("Kielce", "Sienkiewicza", 99999);
        Customer customer = new Customer("Roman", customerAddress);

        ResponseEntity<Customer> getResponse = this.getExistingCustomer();
        ResponseEntity<Customer> postResponse = this.restTemplate
                .postForEntity("/api/v1/customers/", customer, Customer.class);

        MediaType getResponseMimeType = getResponse.getHeaders().getContentType();
        MediaType postResponseMimeType = postResponse.getHeaders().getContentType();

        assertEquals(this.jsonMimeType, getResponseMimeType);
        assertEquals(this.jsonMimeType, postResponseMimeType);
    }

    @Test
    public void testResponseContentTypeIsJsonWhenQueryIsInvalid() {
        Address customerAddress = new Address("Cracow", "Grodzka", 1234567);
        Customer customer = new Customer("Zbigniew", customerAddress);

        ResponseEntity<ApiError> getResponse = this.getErrorBecauseOfNonExistingCustomer();
        ResponseEntity<ApiError> postResponse = this.restTemplate
                .postForEntity("/api/v1/customers/", customer, ApiError.class);

        MediaType getResponseMimeType = getResponse.getHeaders().getContentType();
        MediaType postResponseMimeType = postResponse.getHeaders().getContentType();

        assertEquals(this.jsonMimeType, getResponseMimeType);
        assertEquals(this.jsonMimeType, postResponseMimeType);
    }

    @Test
    public void testOKResponseContentWhenGettingExistingCustomer() {
        ResponseEntity<Customer> response = this.getExistingCustomer();

        assertEquals("Zbigniew", Objects.requireNonNull(response.getBody()).getName());
        assertEquals(this.defaultCustomerAddress, Objects.requireNonNull(response.getBody()).getAddress());
    }

    @Test
    public void testNotFoundResponseContentWhenGettingNonExistingCustomer() {
        ResponseEntity<ApiError> response = this.getErrorBecauseOfNonExistingCustomer();

        assertEquals(HttpStatus.NOT_FOUND , Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals("Customer with id 100 has not been found.",
                Objects.requireNonNull(response.getBody()).getMessage());
    }

    @Test
    public void testCreatedResponseContentWhenPostingNonExistingCustomer() {
        Address customerAddress = new Address("Rzeszów", "Dąbrowskiego", 1313);
        Customer customer = new Customer("Marzena", customerAddress);

        ResponseEntity<Customer> postResponse = this.restTemplate
                .postForEntity("/api/v1/customers/", customer, Customer.class);

        assertEquals("Marzena", Objects.requireNonNull(postResponse.getBody()).getName());
        assertEquals(customerAddress, Objects.requireNonNull(postResponse.getBody()).getAddress());
    }

    @Test
    public void testConflictResponseContentWhenPostingAlreadyExistingCustomer() {
        Address customerAddress = new Address("Cracow", "Grodzka", 1234567);
        Customer customer = new Customer("Zbigniew", customerAddress);

        ResponseEntity<ApiError> postResponse = this.restTemplate
                .postForEntity("/api/v1/customers/", customer, ApiError.class);

        assertEquals(HttpStatus.CONFLICT , Objects.requireNonNull(postResponse.getBody()).getStatus());
        assertEquals("Unable to create! Customer with name Zbigniew and provided address already exists.",
                Objects.requireNonNull(postResponse.getBody()).getMessage());
    }

    private ResponseEntity<Customer> getExistingCustomer() {
        return this.restTemplate.getForEntity("/api/v1/customers/1", Customer.class);
    }

    private ResponseEntity<ApiError> getErrorBecauseOfNonExistingCustomer() {
        return this.restTemplate.getForEntity("/api/v1/customers/100", ApiError.class);
    }
}