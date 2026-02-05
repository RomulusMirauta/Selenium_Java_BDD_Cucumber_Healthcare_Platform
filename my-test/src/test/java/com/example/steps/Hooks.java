package com.example.steps;

import com.example.helpers.driver.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {
    @Before
    public void before() {
        maybeDelay();
        DriverFactory.createDriver();
    }

    @After
    public void after() {
        DriverFactory.quitDriver();
        maybeDelay();
    }

    private static long parseDelayMs() {
        String raw = System.getProperty("TEST_DELAY_MS", System.getenv().getOrDefault("TEST_DELAY_MS", "0"));
        try {
            return Long.parseLong(raw);
        } catch (NumberFormatException ignored) {
            return 0L;
        }
    }

    private static void maybeDelay() {
        long delayMs = parseDelayMs();
        if (delayMs <= 0) {
            return;
        }
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
