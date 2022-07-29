package com.alternativedegree.rest;

import com.alternativedegree.exception.ServiceRuntimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestClient {

    @Value("${mock.server.url}")
    private String url;

    private final RestTemplate rest;

    public RestClient() {
        this.rest = new RestTemplate();
    }

    /**
     * This is a method which makes a get request and returns a string as a response
     * @throws ServiceRuntimeException - when the external service is not available or when external service throws an exception
     */
    public String getSomething() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "*/*");

        HttpEntity<String> requestEntity = new HttpEntity<String>("", headers);
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = rest.exchange(url, HttpMethod.GET, requestEntity, String.class);
        } catch (Exception ex) {
            System.out.println("Exception calling external service: " + ex.getMessage());
            throw new ServiceRuntimeException(ex, "Exception calling external service");
        }

        System.out.println(responseEntity.getBody());
        return responseEntity.getBody();
    }
}
