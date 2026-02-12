package com.irfan.ecommerce.util;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.irfan.ecommerce.base.DriverFactory;

/**
 * GenericActions: Professional Abstraction Layer
 * Features: Self-Healing, Log4j 2, Smart Parsing, and Exception Handling.
 * @author Irfan Muneer
 */
public class GenericActions {

    private static final Logger log = LogManager.getLogger(GenericActions.class);

    private static WebDriver getDriver() { return DriverFactory.getDriver(); }
    private static WebDriverWait getWait() { return new WebDriverWait(getDriver(), Duration.ofSeconds(10)); }
    private static WebDriverWait getShortWait() { return new WebDriverWait(getDriver(), Duration.ofSeconds(2)); }
    private static Actions getActions() { return new Actions(getDriver()); }

    /** Parses String to By object (e.g., "id:login2" or "xpath://button"). */
    private static By parseBy(String locator, String... replacements) {
        String processed = (replacements.length > 0) ? String.format(locator, (Object[]) replacements) : locator;
        if (processed.contains(":")) {
            String[] parts = processed.split(":", 2);
            String strategy = parts[0].toLowerCase().trim();
            String value = parts[1].trim();
            return switch (strategy) {
                case "id" -> By.id(value);
                case "xpath" -> By.xpath(value);
                case "css" -> By.cssSelector(value);
                case "name" -> By.name(value);
                case "class" -> By.className(value);
                case "text" -> By.linkText(value);
                default -> By.xpath(processed);
            };
        }
        return (processed.startsWith("//") || processed.startsWith("(")) ? By.xpath(processed) : By.id(processed);
    }

    /** Core Self-Healing Engine. */
    private static WebElement findElementSmartly(String[] locators, String... replacements) {
        for (String loc : locators) {
            try {
                By by = parseBy(loc, replacements);
                return getShortWait().until(ExpectedConditions.visibilityOfElementLocated(by));
            } catch (Exception e) {
                log.debug("Backup triggered. Failed to find: {}", loc);
            }
        }
        log.error("FATAL: All locators failed: {}", String.join(", ", locators));
        throw new NoSuchElementException("All locators failed.");
    }

    // --- 1. NAVIGATION ---
    public static void navigateTo(String url) {
        try {
            getDriver().get(url);
            log.info("Navigated to: {}", url);
        } catch (Exception e) {
            log.error("Navigation failed to {}: {}", url, e.getMessage());
        }
    }

    public static void refreshPage() {
        try {
            getDriver().navigate().refresh();
            log.info("Page refreshed.");
        } catch (Exception e) {
            log.error("Refresh failed: {}", e.getMessage());
        }
    }

    // --- 2. INTERACTIONS (SELF-HEALING) ---
    public static void click(String[] locators, String... replacements) {
        try {
            WebElement el = findElementSmartly(locators, replacements);
            getWait().until(ExpectedConditions.elementToBeClickable(el)).click();
            log.info("Element clicked successfully.");
        } catch (Exception e) {
            log.error("Click failed: {}", e.getMessage());
            throw e;
        }
    }

    public static void sendKeys(String[] locators, String text, String... replacements) {
        try {
            WebElement el = findElementSmartly(locators, replacements);
            el.clear();
            el.sendKeys(text);
            log.info("Typed [{}] into field.", text);
        } catch (Exception e) {
            log.error("Type failed: {}", e.getMessage());
            throw e;
        }
    }

    public static String getText(String[] locators, String... replacements) {
        try {
            String text = findElementSmartly(locators, replacements).getText();
            log.info("Text retrieved: {}", text);
            return text;
        } catch (Exception e) {
            log.error("GetText failed: {}", e.getMessage());
            return "";
        }
    }

    // --- 3. JAVASCRIPT ---
    public static void jsClick(String[] locators, String... replacements) {
        try {
            WebElement el = findElementSmartly(locators, replacements);
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", el);
            log.info("JS Click performed.");
        } catch (Exception e) {
            log.error("JS Click failed: {}", e.getMessage());
        }
    }

    public static void jsScroll(String[] locators, String... replacements) {
        try {
            WebElement el = findElementSmartly(locators, replacements);
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", el);
            log.info("Scrolled to element via JS.");
        } catch (Exception e) {
            log.error("JS Scroll failed: {}", e.getMessage());
        }
    }

    // --- 4. MOUSE ACTIONS ---
    public static void hover(String[] locators, String... replacements) {
        try {
            getActions().moveToElement(findElementSmartly(locators, replacements)).perform();
            log.info("Mouse hover performed.");
        } catch (Exception e) {
            log.error("Hover failed: {}", e.getMessage());
        }
    }

    public static void doubleClick(String[] locators, String... replacements) {
        try {
            getActions().doubleClick(findElementSmartly(locators, replacements)).perform();
            log.info("Double-click performed.");
        } catch (Exception e) {
            log.error("Double-click failed: {}", e.getMessage());
        }
    }

    // --- 5. DROPDOWNS ---
    public static void selectByText(String[] locators, String text, String... replacements) {
        try {
            new Select(findElementSmartly(locators, replacements)).selectByVisibleText(text);
            log.info("Selected dropdown option: {}", text);
        } catch (Exception e) {
            log.error("Dropdown selection failed: {}", e.getMessage());
        }
    }

    // --- 6. ALERTS ---
    public static void handleAlert(boolean accept) {
        try {
            Alert alert = getWait().until(ExpectedConditions.alertIsPresent());
            if (accept) { alert.accept(); log.info("Alert accepted."); }
            else { alert.dismiss(); log.info("Alert dismissed."); }
        } catch (Exception e) {
            log.warn("No alert found.");
        }
    }

    // --- 7. WINDOWS ---
    public static void switchToWindow(String title) {
        try {
            String current = getDriver().getWindowHandle();
            for (String handle : getDriver().getWindowHandles()) {
                getDriver().switchTo().window(handle);
                if (getDriver().getTitle().contains(title)) {
                    log.info("Switched to window: {}", title);
                    return;
                }
            }
            getDriver().switchTo().window(current);
        } catch (Exception e) {
            log.error("Window switch failed: {}", e.getMessage());
        }
    }

    // --- 8. FRAMES ---
    public static void switchToFrame(String[] locators, String... replacements) {
        try {
            getDriver().switchTo().frame(findElementSmartly(locators, replacements));
            log.info("Switched to iframe.");
        } catch (Exception e) {
            log.error("Frame switch failed: {}", e.getMessage());
        }
    }

    public static void exitFrame() {
        try {
            getDriver().switchTo().defaultContent();
            log.info("Exited frame.");
        } catch (Exception e) {
            log.error("Exit frame failed: {}", e.getMessage());
        }
    }

    // --- 9. VERIFICATION ---
    public static boolean isDisplayed(String[] locators, String... replacements) {
        try {
            boolean visible = findElementSmartly(locators, replacements).isDisplayed();
            log.info("Display status: {}", visible);
            return visible;
        } catch (Exception e) {
            return false;
        }
    }

    // --- 10. SCREENSHOT & COOKIES ---
    public static String takeScreenshot(String name) {
        try {
            File src = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
            String path = System.getProperty("user.dir") + "/screenshots/" + name + "_" + System.currentTimeMillis() + ".png";
            FileUtils.copyFile(src, new File(path));
            log.info("Screenshot saved: {}", path);
            return path;
        } catch (IOException e) {
            log.error("Screenshot failed: {}", e.getMessage());
            return "";
        }
    }

    public static void addCookie(String name, String value) {
        try {
            getDriver().manage().addCookie(new Cookie(name, value));
            getDriver().navigate().refresh();
            log.info("Cookie [{}] added and page refreshed.", name);
        } catch (Exception e) {
            log.error("Failed to add cookie: {}", e.getMessage());
        }
    }
}
