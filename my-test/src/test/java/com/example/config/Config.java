package com.example.config;

public final class Config {
    public static final String BASE_URL =
            System.getProperty("BASE_URL",
                    System.getenv().getOrDefault("BASE_URL", "http://localhost:3001/"));

    public static final boolean HEADLESS = Boolean.parseBoolean(
            System.getProperty("HEADLESS",
                    System.getenv().getOrDefault("HEADLESS", "false")));

    private Config() {
    }
}
