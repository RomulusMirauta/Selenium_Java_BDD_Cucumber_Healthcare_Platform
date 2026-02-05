package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.stream.Collectors;

public class DrugsPage extends BasePage {
    private final By drugsNav = By.xpath("//*[contains(normalize-space(.), 'Drugs') and (self::a or self::button or contains(@class,'db-label'))]");
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
        for (int attempt = 0; attempt < 5; attempt++) {
            List<WebElement> cards = findDrugCardsByName(name);
            if (cards.isEmpty()) {
                return;
            }
            WebElement card = cards.get(0);
            String text = card.getText();
            if (text.contains(description) && text.contains(dosage)) {
                try {
                    WebElement removeButton = card.findElement(By.xpath(".//button[contains(., 'Remove')]"));
                    removeButton.click();
                    acceptAlertIfPresent();
                    new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                            .until(org.openqa.selenium.support.ui.ExpectedConditions.stalenessOf(card));
                } catch (org.openqa.selenium.StaleElementReferenceException ignored) {
                    // element already detached
                }
            } else {
                // If first match doesn't include details, remove it from consideration and try again
                cards.remove(0);
            }
        }
    }
}
