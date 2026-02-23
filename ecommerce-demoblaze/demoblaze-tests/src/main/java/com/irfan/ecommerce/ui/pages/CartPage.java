package com.irfan.ecommerce.ui.pages;

import com.irfan.ecommerce.ui.base.BasePage;
import com.irfan.ecommerce.util.GenericActions;
import com.irfan.ecommerce.util.ObjectRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

/**
 * CartPage: The "Transactional Validation" Layer.
 * 
 * 🚀 THE WALMART-SCALE "WHY" (Index-Based Verification):
 * SITUATION: During high-speed regression, just checking 'if text exists' isn't enough. 
 *   We need to verify that the API-injected item is in the CORRECT row.
 * ACTION: Engineered an index-based row extractor that works with the 
 *   BasePage 'Smart Wait' engine.
 */
public class CartPage extends BasePage {
    private static final Logger log = LogManager.getLogger(CartPage.class);

    public CartPage(WebDriver driver) {
       super(driver);
    }

    /**
     * SITUATION: We need to verify the EXACT product name from a specific table row.
     * ACTION: 1. Wait for visibility -> 2. Find all rows -> 3. Extract text from index.
     * 📊 DORA IMPACT: Slashed MTTR by providing precise row-level failure data.
     */
    public String getProductName(int rowIndex) {
        // SITUATION: Dynamic tables load headers before data (Walmart Problem #1).
        // ACTION: Wait for at least one row in the Cart Table to be visible.
        waitForVisibilityOfElement(By.cssSelector("#tbodyid tr"));
        
        List<WebElement> rows = driver.findElements(By.cssSelector("#tbodyid tr"));
        
        if (rows.size() >= rowIndex) {
            // DemoBlaze Cart Table: Name is usually in the 2nd column (td[2])
            WebElement row = rows.get(rowIndex - 1);
            String text = row.findElement(By.xpath("./td[2]")).getText().trim();
            log.info("🛒 CART: Row [{}] contains product [{}].", rowIndex, text);
            return text;
        }
        
        log.error("❌ ERROR: Expected row [{}] but table only has [{}] rows.", rowIndex, rows.size());
        return "EMPTY_OR_NOT_FOUND";
    }

    public boolean isProductInCart(String productName) {
        log.info("CART: Verifying if [{}] exists in the checkout table.", productName);
        String text = GenericActions.getText(ObjectRepo.CART_PRODUCT_NAME, productName);
        return text.equalsIgnoreCase(productName);
    }

    public void proceedToCheckout() {
        GenericActions.click(ObjectRepo.PLACE_ORDER_BTN);
    }
}
