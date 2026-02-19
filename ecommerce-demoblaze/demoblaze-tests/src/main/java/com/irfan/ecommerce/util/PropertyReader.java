package com.irfan.ecommerce.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * PropertyReader: The central engine for Environment & Element data.
 * 
 * THE WALMART HEADACHE I FIXED:
 * - THE PROBLEM: We had different URLs and different test accounts for QA, Stage, and Prod. 
 *   Changing these in the code every time was slow and caused "Environment Mismatch" errors.
 * - WHAT I DID: I built this dynamic loader. It catches the '-Denv' flag from the Maven command 
 *   and automatically loads the correct config file for that specific environment.
 * - THE RESULT: One command (mvn test -Denv=prod) switches the entire framework 
 *   to Production mode instantly. No code changes needed.
 */
public class PropertyReader {
    private static final Properties prop = new Properties();
    
    static {
        try {
            // Catch the environment from the Maven command, default to 'qa'
            String env = System.getProperty("env", "qa");
            String fileName = "config/" + env + ".properties";
            
            try (InputStream input = PropertyReader.class
                    .getClassLoader().getResourceAsStream(fileName)) {
                if (input == null) {
                    throw new RuntimeException("CONFIG_ERROR: " + fileName + " NOT FOUND in src/main/resources/config/");
                }
                prop.load(input);
            }
        } catch (Exception e) {
            throw new RuntimeException("CRITICAL: Environment Properties load FAILED", e);
        }
    }

    /**
     * Retrieves any value from the properties file (URL, Username, Locators).
     */
    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
}
