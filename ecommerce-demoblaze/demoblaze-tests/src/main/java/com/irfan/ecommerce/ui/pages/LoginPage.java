package com.irfan.ecommerce.ui.pages;

import com.irfan.ecommerce.ui.base.BasePage;
import com.irfan.ecommerce.util.GenericActions;
import com.irfan.ecommerce.util.ObjectRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage extends BasePage {
    private static final Logger log = LogManager.getLogger(LoginPage.class);

    public LoginPage(WebDriver driver) {
        super(driver);
    }

   public LoginPage performLogin(String username, String password, boolean isRegistrationRequired) {
    log.info("🚀 LOGIN ATTEMPT: User [{}]", username);
    try {
        // Sync before clicking
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.elementToBeClickable(GenericActions.getBestLocator(ObjectRepo.NAV_LOGIN)));

        GenericActions.click(ObjectRepo.NAV_LOGIN);
        waitForVisibilityOfElement(ObjectRepo.LOGIN_USER);
        GenericActions.sendKeys(ObjectRepo.LOGIN_USER, username);
        GenericActions.sendKeys(ObjectRepo.LOGIN_PASS, password);
        GenericActions.click(ObjectRepo.LOGIN_BTN);

        // Handle the Alert immediately
        String alertMessage = GenericActions.getAlertTextAndAccept();

        // 🛡️ THE LOGIC GATE: Only register if the test EXPLICITLY expects success
        if (alertMessage.contains("User does not exist") && isRegistrationRequired) {
            log.warn("⚠️ USER MISSING: Registration required for this scenario. Recovering...");
            registerUserIfNew(username, password);
            
            log.info("🔄 RETRYING: Final login attempt.");
            // False here to prevent any possible infinite loop
            return performLogin(username, password, false); 
        }

    } catch (Exception e) {
        log.error("❌ INTERACTION ERROR: {}", e.getMessage());
        throw e;
    }
    return this;
}

    public void registerUserIfNew(String username, String password) {
        try {
            log.info("🛠️ REGISTRATION: Creating account for [{}]", username);
            GenericActions.click(ObjectRepo.NAV_SIGNUP);

            waitForVisibilityOfElement(ObjectRepo.SIGNUP_USERNAME);
            GenericActions.sendKeys(ObjectRepo.SIGNUP_USERNAME, username);
            GenericActions.sendKeys(ObjectRepo.SIGNUP_PASSWORD, password);
            GenericActions.click(ObjectRepo.SIGNUP_BUTTON);

            // Clear the "Sign up successful" alert
            GenericActions.getAlertTextAndAccept();

            // SYNC: Ensure modals are closed before returning control
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    GenericActions.getBestLocator(ObjectRepo.SIGNUP_USERNAME)));

            try {
                wait.until(ExpectedConditions.invisibilityOfElementLocated(
                        GenericActions.getBestLocator(ObjectRepo.MODAL_BACKDROP)));
            } catch (Exception e) {
                log.debug("Backdrop already gone.");
            }

            log.info("✅ SUCCESS: Registration complete.");
        } catch (Exception e) {
            log.error("❌ REGISTRATION_CRASHED: Cannot proceed. {}", e.getMessage());
            throw new RuntimeException("Registration failed, stopping recursive login.");
        }
    }
}