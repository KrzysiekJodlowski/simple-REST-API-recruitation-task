package com.jodlowski.simple_rest_api_recruitation_task.util;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ApiLogger {

    public static void fetchingCustomer(Long id) {
        log.info(String.format("Fetching customer with id %d.", id));
    }

    public static void customerNotFound(Long id) {
        log.error(String.format("Customer with id %d not found!", id));
    }

    public static void sendingCustomer(Long id) {
        log.info(String.format("Sending customer with id %d.", id));
    }

    public static void checkingCustomerExistence(String name) {
        log.info(String.format("Checking for existence of customer with name %s and provided address.",
                name));
    }

    public static void customerAlreadyExists(String name) {
        log.error(String.format("Unable to create! Customer with name %s and provided address already exist.",
                name));
    }

    public static void customerCreated(String name) {
        log.info(String.format("New customer %s created.", name));
    }
}
