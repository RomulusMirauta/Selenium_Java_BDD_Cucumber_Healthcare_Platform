package com.example.helpers.config;

public final class Config {
    public static final String BASE_URL =
            System.getProperty("BASE_URL",
                    System.getenv().getOrDefault("BASE_URL", "http://localhost:3001/"));

        public static final String API_BASE_URL = deriveApiBaseUrl(BASE_URL);

    public static final boolean HEADLESS = Boolean.parseBoolean(
            System.getProperty("HEADLESS",
                    System.getenv().getOrDefault("HEADLESS", "false")));

    private Config() {
    }

        private static String deriveApiBaseUrl(String baseUrl) {
                try {
                        java.net.URI uri = java.net.URI.create(baseUrl);
                        String scheme = uri.getScheme();
                        String authority = uri.getAuthority();
                        if (scheme == null || authority == null) {
                                return baseUrl;
                        }
                        return scheme + "://" + authority;
                } catch (Exception e) {
                        return baseUrl;
                }
        }
}
