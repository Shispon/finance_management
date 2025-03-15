package org.finance_manegement.server.config;

public class DatabaseConfig {
    public static String getDbUrl() {
        return System.getProperty("DB_URL", "jdbc:postgresql://localhost:5432/db");
    }

    public static String getDbUsername() {
        return System.getProperty("DB_USERNAME", "admin");
    }

    public static String getDbPassword() {
        return System.getProperty("DB_PASSWORD", "12345");
    }
}