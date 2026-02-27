package com.irfan.ecommerce.ui.tests;

import com.irfan.ecommerce.ui.base.BaseTest;
import com.irfan.ecommerce.ui.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomePageTest extends BaseTest {

    @Test(description = "Verify HomePage loads correctly and displays categories")
    public void verifyHomePageLanding() {
        // 1. Initialize the Page Object
        HomePage homePage = new HomePage(getDriver());

        // 2. Execute Actions
        homePage.open();
        String title = homePage.getTitleText();

        // 3. Assertions (The 'Test' part)
        Assert.assertEquals(title, "PRODUCT STORE", "Brand logo text mismatch!");
        
        String phoneCategory = homePage.getPhonesCategoryText();
        Assert.assertEquals(phoneCategory, "Phones", "Category sidebar not loaded!");
    }

    @Test(description = "Verify user can navigate to a specific product")
    public void verifyProductNavigation() {
        HomePage homePage = new HomePage(getDriver());
        homePage.open();
        
        // This uses your dynamic locator logic
        homePage.clickProductByName("Laptops");
        
        // Add more assertions here as you build out ProductPage
    }
}