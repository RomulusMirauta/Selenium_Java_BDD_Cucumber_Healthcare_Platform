package com.example.steps;

import com.example.driver.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {
    @Before
    public void before() {
        DriverFactory.createDriver();
    }

    @After
    public void after() {
        DriverFactory.quitDriver();
    }
}
