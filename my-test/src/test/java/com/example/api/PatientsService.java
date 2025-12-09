package com.example.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PatientsService {
    private final String baseUrl;

    public PatientsService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Response addPatient(Object patient, String username, String password) {
        RequestSpecification req = RestAssured.given()
                .baseUri(baseUrl)
                .basePath("api/patients")
                .contentType(ContentType.JSON)
                .body(patient)
                .queryParam("username", username)
                .queryParam("password", password);
        return req.post();
    }

    public Response getAllPatients(String username, String password) {
        RequestSpecification req = RestAssured.given()
                .baseUri(baseUrl)
                .basePath("api/patients")
                .queryParam("username", username)
                .queryParam("password", password);
        return req.get();
    }

    public Response deletePatient(Object id, String username, String password) {
        RequestSpecification req = RestAssured.given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .queryParam("username", username)
                .queryParam("password", password);
        return req.delete(String.format("api/patients/%s", id));
    }
}
