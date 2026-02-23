package com.irfan.ecommerce.ui.pages;

import com.irfan.ecommerce.ui.base.BasePage;
import com.irfan.ecommerce.util.GenericActions;
import com.irfan.ecommerce.util.ObjectRepo;
import org.openqa.selenium.WebDriver;

public class ProductPage extends BasePage {

    public ProductPage(WebDriver driver) {
        super(driver); // IMPACT: Ensures every page uses the same WebDriver session
    }

    public String getProductName() {
        // WALMART MOVE: We wait for the Title before fetching text to avoid 'Empty String' returns
        waitForVisibilityOfElement(ObjectRepo.PRODUCT_TITLE);
        return GenericActions.getText(ObjectRepo.PRODUCT_TITLE);
    }

    public String getProductPrice() {
        return GenericActions.getText(ObjectRepo.PRODUCT_PRICE);
    }

    public void addToCart() {
        GenericActions.click(ObjectRepo.ADD_TO_CART_BTN);
    }
}
