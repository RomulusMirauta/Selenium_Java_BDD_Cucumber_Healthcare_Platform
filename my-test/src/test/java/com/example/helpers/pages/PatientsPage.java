package com.example.helpers.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class PatientsPage extends BasePage {
    private final By patientsNav = By.xpath("//*[contains(normalize-space(.), 'Patients') and (self::a or self::button or contains(@class,'db-label'))]");
    private final By firstNameInput = By.cssSelector("[placeholder='First Name']");
    private final By lastNameInput = By.cssSelector("[placeholder='Last Name']");
    private final By genderInput = By.cssSelector("[placeholder='Gender']");
    private final By addressInput = By.cssSelector("[placeholder='Address']");
    private final By addButton = By.xpath("//button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'add patient')]");

    public PatientsPage(WebDriver driver) {
        super(driver);
    }

    public void gotoPage() {
        click(patientsNav);
    }

    public void addPatient(String firstName, String lastName, String dob, String gender, String address) {
        fill(firstNameInput, firstName);
        fill(lastNameInput, lastName);
        // DOB handling - application might use a date picker; placeholder used in Playwright
        click(By.cssSelector("[title='Fill today']"));
        fill(genderInput, gender);
        fill(addressInput, address);
        click(addButton);
    }

    public List<WebElement> findPatientCardsByName(String name) {
        return driver.findElements(By.xpath("//div[contains(@class,'patients-list')]//div[contains(@class,'patient-card') and contains(., '" + name + "')]") );
    }

    public void removePatientByDetails(String firstName, String lastName) {
        String name = firstName + " " + lastName;
        for (int attempt = 0; attempt < 5; attempt++) {
            List<WebElement> cards = findPatientCardsByName(name);
            if (cards.isEmpty()) {
                return;
            }
            WebElement card = cards.get(0);
            try {
                WebElement removeButton = card.findElement(By.xpath(".//button[contains(., 'Remove')]"));
                removeButton.click();
                acceptAlertIfPresent();
                new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                        .until(org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf(card));
            } catch (org.openqa.selenium.StaleElementReferenceException ignored) {
                // element already detached
            }
        }
    }
}
