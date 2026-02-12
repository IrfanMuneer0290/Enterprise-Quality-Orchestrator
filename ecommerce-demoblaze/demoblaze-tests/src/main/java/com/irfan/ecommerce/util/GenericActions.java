package com.irfan.ecommerce.util;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Set;
import org.openqa.selenium.Cookie;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.irfan.ecommerce.base.DriverFactory;

public class GenericActions {

    private static WebDriver getDriver() { return DriverFactory.getDriver(); }
    private static WebDriverWait getWait() { return new WebDriverWait(getDriver(), Duration.ofSeconds(10)); }
    private static Actions getActions() { return new Actions(getDriver()); }

    // --- 1. BASIC INTERACTIONS (OVERLOADED) ---
    public static void click(By locator) { getWait().until(ExpectedConditions.elementToBeClickable(locator)).click(); }
    public static void click(WebElement element) { getWait().until(ExpectedConditions.elementToBeClickable(element)).click(); }
    
     // --- 2. ADVANCED MOUSE ACTIONS (OVERLOADED) ---
    public static void doubleClick(WebElement element) { getActions().doubleClick(element).perform(); }
    public static void rightClick(WebElement element) { getActions().contextClick(element).perform(); }
    public static void dragAndDrop(WebElement source, WebElement target) { getActions().dragAndDrop(source, target).perform(); }
    public static void moveToElement(WebElement element) { getActions().moveToElement(element).perform(); }

    // --- 3. JAVASCRIPT EXECUTOR ---
    public static void jsClick(WebElement element) { ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", element); }
    public static void jsScrollIntoView(WebElement element) { ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", element); }
    public static void jsType(WebElement element, String text) { ((JavascriptExecutor) getDriver()).executeScript("arguments[0].value='" + text + "';", element); }

    // --- 4. ALERTS ---
    public static void acceptAlert() { getWait().until(ExpectedConditions.alertIsPresent()).accept(); }
    public static void dismissAlert() { getWait().until(ExpectedConditions.alertIsPresent()).dismiss(); }
    public static void typeInAlert(String text) { 
        Alert alert = getWait().until(ExpectedConditions.alertIsPresent());
        alert.sendKeys(text); alert.accept(); 
    }

    // --- 5. WINDOWS & TABS ---
    public static void switchToWindowByTitle(String title) {
        String current = getDriver().getWindowHandle();
        Set<String> all = getDriver().getWindowHandles();
        for (String handle : all) {
            getDriver().switchTo().window(handle);
            if (getDriver().getTitle().equals(title)) return;
        }
        getDriver().switchTo().window(current);
    }

    // --- 6. SELECT / DROPDOWNS (OVERLOADED) ---
    public static void selectByVisibleText(WebElement element, String text) { new Select(element).selectByVisibleText(text); }
    public static void selectByValue(WebElement element, String value) { new Select(element).selectByValue(value); }
    public static void selectByIndex(WebElement element, int index) { new Select(element).selectByIndex(index); }

    // --- 7. FRAMES (OVERLOADED) ---
    public static void switchToFrame(int index) { getDriver().switchTo().frame(index); }
    public static void switchToFrame(String nameOrId) { getDriver().switchTo().frame(nameOrId); }
    public static void switchToFrame(WebElement element) { getDriver().switchTo().frame(element); }
    public static void exitFrame() { getDriver().switchTo().defaultContent(); }

    // --- 8. WAITS & SYNC (OVERLOADED) ---
    public static boolean isDisplayed(By locator) { return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed(); }
    public static void waitForInvisibility(By locator) { getWait().until(ExpectedConditions.invisibilityOfElementLocated(locator)); }

    // --- 9. SCREENSHOT ---
    public static String takeScreenshot(String testName) {
        File src = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        String path = System.getProperty("user.dir") + "/screenshots/" + testName + "_" + System.currentTimeMillis() + ".png";
        try { FileUtils.copyFile(src, new File(path)); } catch (IOException e) { e.printStackTrace(); }
        return path;
    }

    // -- 10. SendKeys (OVERLOADED) ---
    public static void sendKeys(By locator, String text) {  WebElement el = getWait().until(ExpectedConditions.visibilityOfElementLocated(locator)); el.clear(); el.sendKeys(text); }
    public static void sendKeys(WebElement element, String text) {getWait().until(ExpectedConditions.visibilityOf(element));element.clear(); element.sendKeys(text);}

        // --- 10. SECURITY & BYPASS STRATEGIES (FOR PROD/STAGE) ---

    /**
     * Simulation of fetching an OTP via a backend API or Mock Service.
     * In a real project, use RestAssured to call an endpoint like /api/get-otp/{user}
     */
    public static String getOTPFromBackend(String userEmail) {
        System.out.println("LOG: Fetching OTP for " + userEmail + " via Backend API...");
        // Mocking the return of a 6-digit code
        return "123456"; 
    }

    /**
     * Handles 'Invisible CAPTCHA' by injecting a bypass token or using a whitelisted key.
     */
    public static void bypassCaptchaWithToken(WebElement captchaInput, String bypassToken) {
        System.out.println("LOG: Applying CAPTCHA Bypass Token for Automation User...");
        captchaInput.sendKeys(bypassToken);
    }

    /**
     * Helper to add a 'Magic Cookie' that tells the server to skip MFA/CAPTCHA.
     * Many Prod environments use this for whitelisted IP ranges.
     */
    public static void addAutomationBypassCookie(String cookieName, String value) {
        Cookie bypassCookie = new Cookie(cookieName, value);
        getDriver().manage().addCookie(bypassCookie);
        getDriver().navigate().refresh();
    }

}
