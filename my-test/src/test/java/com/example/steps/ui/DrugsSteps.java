package com.example.steps.ui;

import com.example.driver.DriverFactory;
import com.example.pages.DrugsPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;

public class DrugsSteps {
    private WebDriver driver;
    private DrugsPage drugsPage;
    private String currentDrugName;

    // Login step is now centralized in LoginSteps

    @When("the user navigates to the Drugs page and adds a drug with name {string} description {string} dosage {string}")
    public void add_drug_ui(String name, String description, String dosage) {
        driver = DriverFactory.getDriver();
        drugsPage = new DrugsPage(driver);
        drugsPage.gotoPage();
        // Replace placeholder <RANDOM> with a unique value if present
        if (name.contains("<RANDOM>")) {
            name = name.replace("<RANDOM>", String.valueOf(System.currentTimeMillis()));
        }
        currentDrugName = name;
        drugsPage.addDrug(name, description, dosage);
    }

    @Then("the drug is visible in the UI")
    public void drug_visible() {
        driver = DriverFactory.getDriver();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialized. Ensure Hooks.createDriver() runs before step execution.");
        }
        By locator = By.xpath("//*[contains(@class,'drugs-list')]//*[contains(@class,'drug-name') and contains(., '" + currentDrugName + "')]");
        boolean visible;
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
            visible = true;
        } catch (Exception e) {
            visible = false;
        }
        Assert.assertTrue(visible, "Expected drug card to be visible for: " + currentDrugName);
    }

    @When("the user removes the drug")
    public void remove_drug() {
        driver = DriverFactory.getDriver();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialized. Ensure Hooks.createDriver() runs before step execution.");
        }
        if (drugsPage == null) {
            drugsPage = new DrugsPage(driver);
            drugsPage.gotoPage();
        }
        drugsPage.removeDrugByDetails(currentDrugName, "Automated test drug", "10mg");
    }

    @Then("the drug is no longer visible")
    @Then("the drug is not visible in the UI")
    public void drug_not_visible() {
        driver = DriverFactory.getDriver();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialized. Ensure Hooks.createDriver() runs before step execution.");
        }
        By locator = By.xpath("//*[contains(@class,'drugs-list')]//*[contains(@class,'drug-name') and contains(., '" + currentDrugName + "')]");
        boolean exists;
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.invisibilityOfElementLocated(locator));
            exists = false;
        } catch (Exception e) {
            exists = !driver.findElements(locator).isEmpty();
        }
        Assert.assertFalse(exists, "Drug still exists: " + currentDrugName);
    }
}
