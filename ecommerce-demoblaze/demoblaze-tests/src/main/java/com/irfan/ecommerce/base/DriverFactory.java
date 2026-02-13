package com.irfan.ecommerce.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {
    // 1. ThreadLocal container to ensure thread-safety
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
     String headless = System.getProperty("headless");

    public static WebDriver initDriver(String browser) {
    // FIX: Declare 'headless' as a local String variable inside the static method
    String headless = System.getProperty("headless"); 
    
    if (browser.equalsIgnoreCase("chrome")) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        // Logical check: If Maven sends -Dheadless=true, we add the arguments
        if (headless != null && headless.equalsIgnoreCase("true")) {
            options.addArguments("--headless=new"); // For Chrome 109+
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-gpu");
        }
        
        tlDriver.set(new ChromeDriver(options));
    }
    
    getDriver().manage().window().maximize();
    return getDriver();
}

    // 2. Public getter to retrieve the driver for the SPECIFIC thread
    public static synchronized WebDriver getDriver() {
        return tlDriver.get();
    }

    public static void quitDriver() {
        if (getDriver() != null) {
            getDriver().quit();
            tlDriver.remove(); // 3. Crucial: Clear the memory after quit
        }
    }
}
