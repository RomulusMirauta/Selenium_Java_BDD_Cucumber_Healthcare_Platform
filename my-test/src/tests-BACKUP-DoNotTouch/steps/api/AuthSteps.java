package com.example.steps.api;

import com.example.steps.common.SharedContext;
import io.cucumber.java.en.Given;

public class AuthSteps {
    private final SharedContext context;

    public AuthSteps(SharedContext context) {
        this.context = context;
    }

    @Given("the admin credentials are {string} and {string}")
    public void admin_credentials(String user, String pass) {
        context.username = user;
        context.password = pass;
    }
}
