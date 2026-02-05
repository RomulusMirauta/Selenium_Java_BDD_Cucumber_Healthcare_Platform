package com.example.steps.ui.login_access;

import com.example.helpers.config.Config;
import com.example.helpers.driver.DriverFactory;
import com.example.helpers.pages.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;

public class LoginSteps {
    private WebDriver driver;
    private LoginPage loginPage;

    @Given("the application is running")
    public void application_is_running() {
        // No-op or health check could be performed here
    }

    @When("the user navigates to the login page")
    public void navigate_to_login() {
        driver = DriverFactory.getDriver();
        loginPage = new LoginPage(driver, Config.BASE_URL);
        loginPage.gotoPage("/");
    }

    @Given("the user has logged in as {string} with password {string}")
    public void user_has_logged_in(String username, String password) {
        driver = DriverFactory.getDriver();
        loginPage = new LoginPage(driver, Config.BASE_URL);
        loginPage.gotoPage("/");
        loginPage.login(username, password);
    }

    @When("the user logs in with username {string} and password {string}")
    public void user_logs_in(String username, String password) {
        loginPage.login(username, password);
    }

    @Then("the username {string} is visible in the UI")
    public void username_visible(String username) {
        // The Playwright checks text=${username}, use xpath contains text
        driver = DriverFactory.getDriver();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialized. Ensure Hooks.createDriver() runs before step execution.");
        }
        By locator = By.xpath("//*[contains(text(), '" + username + "')]");
        boolean visible;
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
            visible = true;
        } catch (Exception e) {
            visible = false;
        }
        Assert.assertTrue(visible, "Expected username to be visible after login");
    }

    @Then("an unauthorized error is shown")
    public void unauthorized_error() {
        driver = DriverFactory.getDriver();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialized. Ensure Hooks.createDriver() runs before step execution.");
        }
        By locator = By.xpath("//*[contains(text(), '401: Unauthorized') or contains(text(), 'Unauthorized')]");
        boolean visible;
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
            visible = true;
        } catch (Exception e) {
            visible = false;
        }
        Assert.assertTrue(visible, "Expected 401 Unauthorized message to be visible");
    }
}
