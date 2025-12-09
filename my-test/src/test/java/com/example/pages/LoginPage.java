package com.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private final By username = By.cssSelector("[placeholder='Username']");
    private final By password = By.cssSelector("[placeholder='Password']");
    private final By loginButton = By.xpath("//button[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'login')]");

    private final String baseUrl;

    public LoginPage(WebDriver driver, String baseUrl) {
        super(driver);
        this.baseUrl = baseUrl;
    }

    public void gotoPage(String path) {
        driver.get(baseUrl + path);
    }

    public void login(String user, String pass) {
        fill(username, user);
        fill(password, pass);
        click(loginButton);
    }
}
