package com.example.steps.e2e;

import com.example.config.Config;
import com.example.driver.DriverFactory;
import com.example.pages.PatientsPage;
import com.example.pages.LoginPage;
import com.example.utils.DBUtils;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class E2ePatientsSteps {
    private WebDriver driver;
    private PatientsPage patientsPage;
    private String firstName;
    private String lastName;

    @When("the user navigates to the Patients page and adds a patient with firstName {string} lastName {string} dob {string} gender {string} address {string}")
    public void add_patient_ui(String firstName, String lastName, String dob, String gender, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        driver = DriverFactory.getDriver();
        LoginPage loginPage = new LoginPage(driver, Config.BASE_URL);
        loginPage.gotoPage("/");
        patientsPage = new PatientsPage(driver);
        patientsPage.gotoPage();
        patientsPage.addPatient(firstName, lastName, dob, gender, address);
    }

    @Then("the patient is visible in the UI")
    public void patient_visible() {
        driver = DriverFactory.getDriver();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialized. Ensure Hooks.createDriver() runs before step execution.");
        }
        String fullName = firstName + " " + lastName;
        boolean visible = !driver.findElements(By.xpath("//*[contains(@class,'patient-name') and contains(., '" + fullName + "')]"))
            .isEmpty() || !driver.findElements(By.xpath("//*[contains(@class,'patient-card') and contains(., '" + fullName + "')]"))
            .isEmpty();
        Assert.assertTrue(visible, "Expected patient card to be visible");
    }

    @When("the user removes all matching patients with firstName {string} lastName {string}")
    public void remove_matching_patients(String firstName, String lastName) {
        driver = DriverFactory.getDriver();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialized. Ensure Hooks.createDriver() runs before step execution.");
        }
        if (patientsPage == null) {
            patientsPage = new PatientsPage(driver);
            patientsPage.gotoPage();
        }
        patientsPage.removePatientByDetails(firstName, lastName);
    }

    @Then("the patient is not visible in the UI")
    public void patient_not_visible() {
        driver = DriverFactory.getDriver();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialized. Ensure Hooks.createDriver() runs before step execution.");
        }
        String fullName = firstName + " " + lastName;
        By nameLocator = By.xpath("//*[contains(@class,'patient-name') and contains(., '" + fullName + "')]");
        By cardLocator = By.xpath("//*[contains(@class,'patient-card') and contains(., '" + fullName + "')]");
        try {
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated(nameLocator));
        } catch (Exception ignored) {
        }
        boolean exists = !driver.findElements(nameLocator).isEmpty() || !driver.findElements(cardLocator).isEmpty();
        Assert.assertFalse(exists, "Patient still exists in UI");
    }

    @Then("the patient is not present in the DB")
    public void patient_not_present_db() {
        try {
            List<Map<String, Object>> rows = DBUtils.query("SELECT * FROM Patients WHERE FirstName = ? AND LastName = ?", firstName, lastName);
            Assert.assertTrue(rows.isEmpty(), "Expected no matching rows in DB");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
