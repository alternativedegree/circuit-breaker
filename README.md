This project implements CircuitBreaker using [resilience4j](https://resilience4j.readme.io/docs/circuitbreaker)

### Steps to test

- Start the mock server - clone the below repo and start the Spring Boot application
    -   `git@github.com:alternativedegree/mock-server.git`
- Start the client(which is the current application), by running `Application.java`
- Try to access the `hello` endpoint from a rest client like Postman. 
  You should receive a HTTP status code of 200 
    - endpoint url would be `http://localhost:8888/test/hello`
- Stop the mock server
- Hit the `hello` endpoint multiple times
- After about 5 attempts or so, the response body should change from
  `Internal Server Error` to `Fail fast. Try Again!`
  which indicates the CircuitBreaker is in OPEN state
- As long as the mock server is down, CircuitBreaker should oscillate
  between OPEN state(with response body as `Fail fast. Try Again!`)
  and HALF-OPEN state(with response body as `Internal Server Error`) 
