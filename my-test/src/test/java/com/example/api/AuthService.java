package com.example.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AuthService {
    private final String baseUrl;

    public AuthService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Response login(String username, String password) {
        RequestSpecification req = RestAssured.given()
                .baseUri(baseUrl)
                .basePath("api/login")
                .contentType("application/json")
                .body(String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password));
        return req.post();
    }
}
