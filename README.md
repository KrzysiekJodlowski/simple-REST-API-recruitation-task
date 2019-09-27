
# Simple REST API in Spring - Recruitation task

1. Task - Implement simple service with REST API:
- This service should allow add new customer or get details about customer by id. 
- This service should be very simple, because of this please keep data in memory (it can be in map). 
- It should be possible to access this service by everyone, do not implement authentication. 
- I want to check only if you are able to write REST API, preferred in Java, and you can use technology which you want. 
- Customer should have: id (type: number), name, address(city, street, zip code).

2. API quick reference:
- for sending and retrieving customer JSON format is used
- supported queries:

  - #### for GETTING customer:    
  
  ```
  http method: GET,
  uri: /api/v1/customers/{id}
  ``` 
  where "id" is a customer id. Example response (with status ```200 OK```) when the customer exists: 
  ```json
  {
    "id": 1,
    "name": "Alex",
    "address": {
        "id": 1,
        "city": "New York",
        "street": "Main",
        "zipCode": 123456
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/v1/customers/1"
        }
    }
    ```
    When customer does not exist response (with status code ```404 NOT_FOUND```) looks like that:
    ```json
  {
    "status": "NOT_FOUND",
    "message": "Customer with id 1 has not been found."
  }
    ```    
    
  - #### for ADDING customer:    
  
  ```
  http method: POST 
  uri: /api/v1/customers/
  ```
  In the body of a request customer data (without ids) should be passed:
  ```json
  {
    "name": "Alex",
    "address": {
        "city": "New York",
        "street": "Main",
        "zipCode": 123456
    }
  }
  ```
  In a response API user gets the same info as when searching by id, only status code is now ```201 CREATED```. <br />API user can't add customer with the same data - response (with status ```409 CONFLICT```) looks like:
  ```json
  {
    "status": "CONFLICT",
    "message": "Unable to create! Customer with name Alex and provided address already exists."
  }
  ```
3. Technologies used in the project:
- Java 8
- Spring Boot
- Spring Data JPA
- H2 in-memory database
- Lombok (for data and logging)
- Spring HATEOAS
- For testing: MockMvc, DataJpatest, junit, assertj, mockito
