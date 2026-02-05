package com.example.helpers.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public class DriverFactory {
    private static final ThreadLocal<WebDriver> THREAD_DRIVER = new ThreadLocal<>();
    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);

    public static void createDriver() {
        if (THREAD_DRIVER.get() == null) {
            var wdm = WebDriverManager.chromedriver();
            // Allow CI / local override of the driver binary via CHROME_DRIVER_PATH or driver version via CHROME_DRIVER_VERSION
            String pinnedPathEnv = System.getenv("CHROME_DRIVER_PATH");
            String pinnedVersion = System.getenv("CHROME_DRIVER_VERSION");
            boolean driverSetByPinnedPath = false;
            if (pinnedPathEnv != null && !pinnedPathEnv.isEmpty()) {
                log.info("Using pinned chromedriver path from CHROME_DRIVER_PATH={}", pinnedPathEnv);
                if (isChromedriverValid(pinnedPathEnv)) {
                    System.setProperty("webdriver.chrome.driver", pinnedPathEnv);
                    driverSetByPinnedPath = true;
                } else {
                    log.warn("Pinned chromedriver at '{}' is invalid; falling back to WebDriverManager (auto-download)", pinnedPathEnv);
                    driverSetByPinnedPath = false;
                }
            }
            // Only honor pinned version if we didn't already set a path using CHROME_DRIVER_PATH
            if (!driverSetByPinnedPath && pinnedVersion != null && !pinnedVersion.isEmpty()) {
                log.info("Using pinned ChromeDriver version from CHROME_DRIVER_VERSION={}. Forcing a fresh download.", pinnedVersion);
                try {
                    // Force-download to avoid using an incompatible cached binary
                    wdm.driverVersion(pinnedVersion).forceDownload().setup();
                } catch (Exception t) {
            log.warn("Failed to fetch pinned ChromeDriver version {}: {}", pinnedVersion, t.getMessage());
                    log.warn("Falling back to Selenium Manager auto-detection.");
                }
            } else if (!driverSetByPinnedPath) {
                log.info("No pinned ChromeDriver provided. Using Selenium Manager for driver resolution.");
            }
            String driverPath = System.getProperty("webdriver.chrome.driver");
        log.info("WebDriverManager webdriver.chrome.driver = {}", driverPath);
            ChromeOptions options = new ChromeOptions();
            // Default to headless if CI environment variable set
            String headless = System.getProperty("HEADLESS", System.getenv().getOrDefault("HEADLESS", "false"));
            if ("true".equalsIgnoreCase(headless)) {
                options.addArguments("--headless=new");
            }
            THREAD_DRIVER.set(new ChromeDriver(options));
        }
    }

    private static boolean isChromedriverValid(String pathStr) {
        try {
            Path path = Path.of(pathStr);
            if (!Files.exists(path) || Files.isDirectory(path)) {
                return false;
            }
            // Run the binary with --version to verify it's a chromedriver binary and that it runs
            ProcessBuilder pb = new ProcessBuilder(pathStr, "--version");
            pb.redirectErrorStream(true);
            Process proc = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                String line = reader.readLine();
                if (line == null) return false;
                // Typical output: "ChromeDriver 114.0.5735.90 (some commit)"
                if (line.toLowerCase(Locale.ROOT).contains("chromedriver")) {
                    return true;
                }
            }
            return proc.waitFor() == 0;
    } catch (java.io.IOException | InterruptedException t) {
            // If anything goes wrong, treat the driver as invalid so we fallback to auto-download
            return false;
        }
    }

    public static WebDriver getDriver() {
        return THREAD_DRIVER.get();
    }

    public static void quitDriver() {
        WebDriver driver = THREAD_DRIVER.get();
        if (driver != null) {
            driver.quit();
            THREAD_DRIVER.remove();
        }
    }

    // Helper to set a driver for the current thread. Useful in unit tests where you want to inject a mock WebDriver.
    public static void setDriverForThread(WebDriver driver) {
        THREAD_DRIVER.set(driver);
    }

    // Remove a driver for the current thread (test teardown / cleanup helper)
    public static void clearDriverForThread() {
        THREAD_DRIVER.remove();
    }
}
