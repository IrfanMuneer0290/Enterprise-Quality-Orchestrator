package com.irfan.ecommerce.ui.pages;

import com.irfan.ecommerce.util.GenericActions;
import com.irfan.ecommerce.util.ObjectRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CartPage {
    private static final Logger log = LogManager.getLogger(CartPage.class);

    public CartPage() {
        // Utilizing GenericActions, so no driver storage needed
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
