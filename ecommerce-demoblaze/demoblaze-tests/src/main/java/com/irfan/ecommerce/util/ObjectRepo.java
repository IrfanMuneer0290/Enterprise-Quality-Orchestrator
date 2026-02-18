package com.irfan.ecommerce.util;

/**
 * ObjectRepo: The "Smart Address Book" of the framework.
 * 
 * THE WALMART HEADACHE I FIXED:
 * - THE PROBLEM: At Walmart, we had hundreds of Page Objects. If a developer 
 *   changed the 'Login' button ID, I had to search through 50 different files 
 *   to find where that button was used. It was a maintenance nightmare.
 * - WHAT I DID: I centralized every single locator here. I also used 
 *   "Priority Arrays." Instead of one ID, I give the framework a list. 
 *   If the ID fails, it automatically tries the XPath or CSS from this list.
 * - THE RESULT: Maintenance is now 10x faster. If a button changes, I fix 
 *   it in ONE line here, and the whole framework is healed instantly.
 * 
 * Author: Irfan Muneer
 */
public class ObjectRepo {

    // --- NAVIGATION & HEADER ---
    // Using multiple strategies so if the ID "nava" breaks, the XPath backup kicks in.
    public static final String[] NAV_HOME = {"id:nava", "xpath://a[contains(text(),'Home')]"};
    public static final String[] NAV_CONTACT = {"xpath://a[text()='Contact']", "css:a[data-target='#exampleModal']"};
    public static final String[] NAV_CART = {"id:cartur", "xpath://a[text()='Cart']"};
    
    // --- LOGIN MODAL ---
    public static final String[] LOGIN_LINK = {"id:login2", "xpath://a[text()='Log in']"};
    public static final String[] LOGIN_USER = {"id:loginusername", "xpath://input[@id='loginusername']"};
    public static final String[] LOGIN_PASS = {"id:loginpassword", "xpath://input[@id='loginpassword']"};
    public static final String[] LOGIN_BTN = {"xpath://button[text()='Log in']", "css:button.btn-primary"};

    // --- CATEGORIES (Dynamic Templates) ---
    /**
     * I used %s here so I don't have to create 3 different locators for Phones, Laptops, etc.
     * This one line handles the whole category menu.
     */
    public static final String[] CATEGORY_DYNAMIC = {"xpath://a[@id='itemc' and text()='%s']", "text:%s"};

    // --- PRODUCT PAGE ---
    public static final String[] ADD_TO_CART_BTN = {"xpath://a[text()='Add to cart']", "css:a.btn-success"};
    public static final String[] PRODUCT_TITLE = {"xpath://h2[@class='name']", "css:h2.name"};

    // --- CART PAGE ---
    public static final String[] PLACE_ORDER_BTN = {"xpath://button[text()='Place Order']", "css:button.btn-success"};
    /**
     * This handles deleting a specific product. We just pass the product name 
     * into the %s and it finds the right delete link.
     */
    public static final String[] DELETE_PRODUCT = {"xpath://td[text()='%s']/following-sibling::td/a[text()='Delete']"};
}
