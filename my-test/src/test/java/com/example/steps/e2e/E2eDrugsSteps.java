package com.example.steps.e2e;

import com.example.helpers.driver.DriverFactory;
import com.example.helpers.pages.DrugsPage;
import com.example.helpers.utils.DBUtils;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class E2eDrugsSteps {
    private WebDriver driver;
    private DrugsPage drugsPage;
    private String name;

    @When("the user removes all matching drugs with name {string} description {string} dosage {string}")
    public void remove_matching_drugs(String name, String description, String dosage) {
        this.name = name;
        driver = DriverFactory.getDriver();
        if (drugsPage == null) {
            drugsPage = new DrugsPage(driver);
        }
        drugsPage.removeDrugByDetails(name, description, dosage);
    }

    @Then("the drug is not present in the DB")
    public void drug_not_present_db() {
        try {
            List<Map<String, Object>> rows = DBUtils.query("SELECT * FROM Drugs WHERE Name = ?", name);
            Assert.assertTrue(rows.isEmpty(), "Expected no matching rows in DB");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
