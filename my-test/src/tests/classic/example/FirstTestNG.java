package com.example;

import org.testng.annotations.Test; // Imports only the @Test annotation from TestNG.
// import org.testng.annotations.*; // Imports all annotations from the org.testng.annotations package (e.g., @Test, @BeforeClass, @AfterMethod, etc.).
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class FirstTestNG {
    @Test
    public void testGetRequest() {
        given()
            .baseUri("https://jsonplaceholder.typicode.com")
        .when()
            .get("/posts/1")
        .then()
            .statusCode(200)
            .body("id", equalTo(1));
    }
}