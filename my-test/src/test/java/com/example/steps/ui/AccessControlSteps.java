package com.example.steps.ui;

import com.example.driver.DriverFactory;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.Arrays;
import java.util.List;

public class AccessControlSteps {
    private WebDriver driver;

    // Login is centralized in LoginSteps; reuse the central login step

    @Then("the user should see sections {string} and should NOT see {string}")
    public void user_should_see_sections_and_not_see(String shouldSeeCsv, String shouldNotSeeCsv) {
        // Ensure driver is initialized by the common Hooks; fall back to creating one if it isn't.
        driver = DriverFactory.getDriver();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialized. Ensure Hooks.createDriver() runs before step execution.");
        }

        List<String> shouldSee = Arrays.asList(shouldSeeCsv.split(","));
        List<String> shouldNotSee = Arrays.asList(shouldNotSeeCsv.split(","));
        for (String s : shouldSee) {
            String trimmed = s.trim();
            if (trimmed.isEmpty()) continue;
            boolean exists = !driver.findElements(By.xpath("//*[contains(., '" + trimmed + "')]")).isEmpty();
            Assert.assertTrue(exists, "Expected to see section: " + trimmed);
        }
        for (String s : shouldNotSee) {
            String trimmed = s.trim();
            if (trimmed.isEmpty()) continue;
            boolean exists = !driver.findElements(By.xpath("//*[contains(., '" + trimmed + "')]")).isEmpty();
            Assert.assertFalse(exists, "Expected not to see section: " + trimmed);
        }
    }
}
