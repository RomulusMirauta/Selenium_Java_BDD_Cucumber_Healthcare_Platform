package com.example.helpers.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    private final int defaultWait = 10;
    private static final long DEMO_DELAY_MS = parseDelayMs();

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    protected WebElement waitForVisible(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(defaultWait));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        maybeDelay();
        waitForVisible(locator).click();
    }

    protected void fill(By locator, String text) {
        maybeDelay();
        WebElement el = waitForVisible(locator);
        el.clear();
        el.sendKeys(text);
    }

    public String getText(By locator) {
        maybeDelay();
        return waitForVisible(locator).getText();
    }

    protected void acceptAlertIfPresent() {
        try {
            maybeDelay();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
        } catch (Exception ignored) {
            // No alert present within timeout; ignore.
        }
    }

    private static long parseDelayMs() {
        String raw = System.getProperty("DEMO_DELAY_MS", System.getenv().getOrDefault("DEMO_DELAY_MS", "0"));
        try {
            return Long.parseLong(raw);
        } catch (NumberFormatException ignored) {
            return 0L;
        }
    }

    private static void maybeDelay() {
        if (DEMO_DELAY_MS <= 0) {
            return;
        }
        try {
            Thread.sleep(DEMO_DELAY_MS);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
