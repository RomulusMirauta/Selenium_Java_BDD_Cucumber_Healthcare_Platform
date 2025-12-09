package com.example.steps.api;

import com.example.config.Config;
import io.cucumber.java.Before;
import io.restassured.RestAssured;

public class ApiHooks {
    @Before
    public void setBaseUri() {
        RestAssured.baseURI = Config.BASE_URL;
    }
}
