package com.example.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class DrugsService {
    private final String baseUrl;

    public DrugsService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Response addDrug(Object drug, String username, String password) {
        RequestSpecification req = RestAssured.given()
                .baseUri(baseUrl)
                .basePath("api/drugs")
                .contentType(ContentType.JSON)
                .body(drug)
                .queryParam("username", username)
                .queryParam("password", password);
        return req.post();
    }

    public Response getAllDrugs(String username, String password) {
        RequestSpecification req = RestAssured.given()
                .baseUri(baseUrl)
                .basePath("api/drugs")
                .queryParam("username", username)
                .queryParam("password", password);
        return req.get();
    }

    public Response deleteDrug(Object id, String username, String password) {
        RequestSpecification req = RestAssured.given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .queryParam("username", username)
                .queryParam("password", password);
        return req.delete(String.format("api/drugs/%s", id));
    }
}
