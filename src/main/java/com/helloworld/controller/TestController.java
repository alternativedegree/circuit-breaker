package com.helloworld.controller;

import com.helloworld.circuitbreaker.CircuitBreakerFactory;
import com.helloworld.rest.RestClient;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    RestClient restClient;

    @Autowired
    CircuitBreakerFactory circuitBreakerFactory;

    @GetMapping(path = "/hello")
    ResponseEntity<String> sayHello( ) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.getCircuitBreaker();
        String result = null;
        try{
            result = circuitBreaker.executeSupplier(restClient::getSomething);
        } catch (CallNotPermittedException ex) {
            System.out.println("Circuit breaker is in open state: "+ex.getMessage());
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fail fast. Try Again!");
        } catch (Exception ex) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }

        return ResponseEntity.status(200).body(result);
    }
}
