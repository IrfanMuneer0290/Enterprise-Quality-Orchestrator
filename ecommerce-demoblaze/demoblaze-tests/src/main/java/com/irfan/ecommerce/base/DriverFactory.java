package com.irfan.ecommerce.base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {
    // 1. ThreadLocal container to ensure thread-safety
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    public static WebDriver initDriver(String browser) {
        System.out.println("LOG: Initializing " + browser + " for Thread: " + Thread.currentThread().getId());
        
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            // Headless is great for CI/CD pipelines
            // options.addArguments("--headless"); 
            tlDriver.set(new ChromeDriver(options));
        }
        
        getDriver().manage().window().maximize();
        getDriver().manage().deleteAllCookies();
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
