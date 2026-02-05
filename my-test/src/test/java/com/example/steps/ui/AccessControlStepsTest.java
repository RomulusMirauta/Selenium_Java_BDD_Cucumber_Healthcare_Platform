package com.example.steps.ui;

import com.example.driver.DriverFactory;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AccessControlStepsTest {

    private WebDriver mockDriver;

    @BeforeMethod
    // @SuppressWarnings({"unchecked","rawtypes"})
    @SuppressWarnings({"unchecked"})
    public void setUp() throws Exception {
        mockDriver = Mockito.mock(WebDriver.class);

        // Statically inject our mock into DriverFactory's THREAD_DRIVER ThreadLocal
        Field field = DriverFactory.class.getDeclaredField("THREAD_DRIVER");
        field.setAccessible(true);
        ThreadLocal<WebDriver> threadLocal = (ThreadLocal<WebDriver>) field.get(null);
        threadLocal.set(mockDriver);
    }

    @AfterMethod
    // @SuppressWarnings({"unchecked","rawtypes"})
    @SuppressWarnings({"unchecked"})
    public void tearDown() throws Exception {
        // Clear ThreadLocal driver
        Field field = DriverFactory.class.getDeclaredField("THREAD_DRIVER");
        field.setAccessible(true);
        ThreadLocal<WebDriver> threadLocal = (ThreadLocal<WebDriver>) field.get(null);
        threadLocal.remove();
    }

    @Test
    public void testUserShouldSeeSectionsAndNotSee() {
        AccessControlSteps steps = new AccessControlSteps();

        WebElement visibleElement = Mockito.mock(WebElement.class);
        when(mockDriver.findElements(any(By.class))).thenAnswer(invocation -> {
            By by = invocation.getArgument(0);
            String byStr = by.toString();
            if (byStr.contains("Drugs")) {
                return Arrays.asList(visibleElement);
            }
            if (byStr.contains("Patients")) {
                return Collections.emptyList();
            }
            return Collections.emptyList();
        });

        // Should not throw any AssertionError
        steps.user_should_see_sections_and_not_see("Drugs", "Patients");
    }
}
