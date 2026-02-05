package com.example.helpers.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.example.steps"},
        plugin = {"pretty", "html:target/cucumber-report.html"}
)
public class CucumberTestNGRunner extends AbstractTestNGCucumberTests {
}
