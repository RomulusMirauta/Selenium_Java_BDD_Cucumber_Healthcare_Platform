package com.example.steps.ui;

import com.example.driver.DriverFactory;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class LogoutSteps {
    private WebDriver driver;

    @When("the user clicks logout")
    public void user_clicks_logout() {
        driver = DriverFactory.getDriver();
        driver.findElement(By.xpath("//*[contains(., 'Logout') or contains(., 'logout')]")).click();
    }

    @Then("the login button should be visible")
    public void login_button_visible() {
        driver = DriverFactory.getDriver();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialized. Ensure Hooks.createDriver() runs before step execution.");
        }
        boolean visible = !driver.findElements(By.xpath("//button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'login')]")).isEmpty();
        Assert.assertTrue(visible, "Expected login button to be visible after logout");
    }
}
