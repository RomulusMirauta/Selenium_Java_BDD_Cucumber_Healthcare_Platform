package com.example.config;

public class DbConfig {
    public static final String DB_USER = System.getProperty("DB_USER", System.getenv().getOrDefault("DB_USER", "sa"));
    public static final String DB_PASSWORD = System.getProperty("DB_PASSWORD", System.getenv().getOrDefault("DB_PASSWORD", "sa57843hFL^%*#"));
    public static final String DB_SERVER = System.getProperty("DB_SERVER", System.getenv().getOrDefault("DB_SERVER", "localhost"));
    public static final String DB_NAME = System.getProperty("DB_NAME", System.getenv().getOrDefault("DB_NAME", "HealthcareDB"));
    public static String getConnectionString() {
        return String.format("jdbc:sqlserver://%s;database=%s;user=%s;password=%s;encrypt=false;", DB_SERVER, DB_NAME, DB_USER, DB_PASSWORD);
    }
}
