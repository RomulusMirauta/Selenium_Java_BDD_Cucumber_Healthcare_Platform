package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.stream.Collectors;

public class DrugsPage extends BasePage {
    private final By drugsNav = By.cssSelector(".db-label:has-text('Drugs')");
    private final By nameInput = By.cssSelector("[placeholder='Drug Name']");
    private final By descriptionInput = By.cssSelector("[placeholder='Description']");
    private final By dosageInput = By.cssSelector("[placeholder='Dosage']");
    private final By addButton = By.xpath("//button[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'add drug')]");
    private final By drugCards = By.cssSelector(".drugs-list .drug-card");

    public DrugsPage(WebDriver driver) {
        super(driver);
    }

    public void gotoPage() {
        click(drugsNav);
    }

    public void addDrug(String name, String description, String dosage) {
        fill(nameInput, name);
        fill(descriptionInput, description);
        fill(dosageInput, dosage);
        click(addButton);
    }

    public List<WebElement> findDrugCardsByName(String name) {
        List<WebElement> allCards = driver.findElements(drugCards);
        return allCards.stream()
                .filter(c -> c.getText() != null && c.getText().contains(name))
                .collect(Collectors.toList());
    }

    public void removeDrugByDetails(String name, String description, String dosage) {
        List<WebElement> cards = findDrugCardsByName(name);
        for (WebElement card : cards) {
            String text = card.getText();
            if (text.contains(description) && text.contains(dosage)) {
                card.findElement(By.xpath(".//button[contains(., 'Remove')]")).click();
            }
        }
    }
}
