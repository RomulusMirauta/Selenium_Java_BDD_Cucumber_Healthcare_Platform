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

public class DrugsStepsTest {
    private WebDriver mockDriver;

    @BeforeMethod
    @SuppressWarnings({"unchecked"})
    public void setUp() throws Exception {
        mockDriver = Mockito.mock(WebDriver.class);
        Field field = DriverFactory.class.getDeclaredField("THREAD_DRIVER");
        field.setAccessible(true);
        ThreadLocal<WebDriver> threadLocal = (ThreadLocal<WebDriver>) field.get(null);
        threadLocal.set(mockDriver);
    }

    @AfterMethod
    @SuppressWarnings({"unchecked"})
    public void tearDown() throws Exception {
        Field field = DriverFactory.class.getDeclaredField("THREAD_DRIVER");
        field.setAccessible(true);
        ThreadLocal<WebDriver> threadLocal = (ThreadLocal<WebDriver>) field.get(null);
        threadLocal.remove();
    }

    @Test
    public void testDrugVisible() throws Exception {
        DrugsSteps steps = new DrugsSteps();
        String drugName = "MyTestDrug";
        // Inject as private field currentDrugName
        Field f = DrugsSteps.class.getDeclaredField("currentDrugName");
        f.setAccessible(true);
        f.set(steps, drugName);

        WebElement visibleElement = Mockito.mock(WebElement.class);
        when(mockDriver.findElements(any(By.class))).thenAnswer(invocation -> {
            By by = invocation.getArgument(0);
            if (by.toString().contains(drugName)) {
                return Arrays.asList(visibleElement);
            }
            return Collections.emptyList();
        });

        // Should not throw
        steps.drug_visible();
    }

    @Test
    public void testDrugNotVisible() throws Exception {
        DrugsSteps steps = new DrugsSteps();
        String drugName = "MyTestDrugNotPresent";
        Field f = DrugsSteps.class.getDeclaredField("currentDrugName");
        f.setAccessible(true);
        f.set(steps, drugName);

        when(mockDriver.findElements(any(By.class))).thenReturn(Collections.emptyList());

        // Should not throw
        steps.drug_not_visible();
    }
}
