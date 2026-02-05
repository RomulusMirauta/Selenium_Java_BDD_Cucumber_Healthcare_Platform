package com.example.steps.ui;

import com.example.driver.DriverFactory;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;

public class LogoutSteps {
    private WebDriver driver;

    @When("the user clicks logout")
    public void user_clicks_logout() {
        driver = DriverFactory.getDriver();
        By logoutLocator = By.xpath("//*[contains(@class,'db-label') and contains(normalize-space(.), 'Logout')]");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(logoutLocator));
            el.click();
        } catch (Exception e) {
            // Retry once in case of stale element
            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(logoutLocator));
            el.click();
        }
    }

    @Then("the login button should be visible")
    public void login_button_visible() {
        driver = DriverFactory.getDriver();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialized. Ensure Hooks.createDriver() runs before step execution.");
        }
        try {
            driver.switchTo().alert().accept();
        } catch (Exception ignored) {
            // no alert present
        }
        boolean visible = !driver.findElements(By.xpath("//button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'login')]")).isEmpty();
        Assert.assertTrue(visible, "Expected login button to be visible after logout");
    }
}
