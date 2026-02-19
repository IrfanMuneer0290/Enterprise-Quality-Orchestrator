package com.irfan.ecommerce.util;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * ConfigReader: Handles Environment Orchestration.
 * 
 * THE WALMART HEADACHE I FIXED:
 * - THE PROBLEM: Even with Maven variables, if the Java code doesn't know how 
 *   to map "-Denv=prod" to "prod.properties", the build fails. 
 * - WHAT I DID: I built this dynamic loader. It catches the Maven variable 
 *   at runtime and loads the specific property file for that environment.
 * - THE RESULT: 100% Environment Portability. The same artifact runs in 
 *   Dev, QA, and Prod without a single line of code change.
 */
public class ConfigReader {
    private static Properties prop;

    static {
        try {
            // "env" comes directly from your mvn -Denv command
            String env = System.getProperty("env", "qa"); 
            String path = "src/test/resources/config/" + env + ".properties";
            
            FileInputStream fis = new FileInputStream(path);
            prop = new Properties();
            prop.load(fis);
        } catch (Exception e) {
            // Fallback or Error handling if the file isn't found
            System.err.println("CRITICAL: Environment config not found for: " + System.getProperty("env"));
        }
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
}
