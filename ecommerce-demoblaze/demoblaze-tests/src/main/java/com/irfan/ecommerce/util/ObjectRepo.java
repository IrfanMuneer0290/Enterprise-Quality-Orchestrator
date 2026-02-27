package com.irfan.ecommerce.util;

/**
 * ObjectRepo: The "Smart Address Book" of the framework.
 */
public class ObjectRepo {

    private ObjectRepo() {
        throw new IllegalStateException("Utility class - instantiation is not allowed.");
    }

    // --- NAVIGATION & HEADER ---
    public static final String[] NAV_HOME = { "id:nava", "xpath://a[contains(text(),'Home')]" };
    public static final String[] NAV_USER = { "id:nameofuser", "xpath://a[@id='nameofuser']" };
    public static final String[] NAV_CART = { "id:cartur", "xpath://a[text()='Cart']" };

    // --- SIGN UP MODAL ---
    public static final String[] NAV_SIGNUP = { "id:signin2", "xpath://a[text()='Sign up']" };
    public static final String[] SIGNUP_USERNAME = { "id:sign-username" };
    public static final String[] SIGNUP_PASSWORD = { "id:sign-password" };
    public static final String[] SIGNUP_BUTTON = { "xpath://button[text()='Sign up']" };

    // --- LOGIN MODAL ---
    public static final String[] NAV_LOGIN = { "id:login2", "xpath://a[text()='Log in']" };
    public static final String[] LOGIN_USER = { "id:loginusername" };
    public static final String[] LOGIN_PASS = { "id:loginpassword" };
    public static final String[] LOGIN_BTN = { "xpath://button[text()='Log in']" };

    // --- CATEGORIES (Dynamic Templates) ---
    public static final String[] CATEGORY_DYNAMIC = { "xpath://a[text()='%s']" };

    // --- UI OVERLAYS ---
    public static final String[] MODAL_BACKDROP = { "class:modal-backdrop", "xpath://div[contains(@class,'modal-backdrop')]" };

    // --- PRODUCT PAGE ---
    public static final String[] ADD_TO_CART_BTN = { "xpath://a[text()='Add to cart']", "css:a.btn-success" };
    public static final String[] PRODUCT_TITLE = { "xpath://h2[@class='name']" };
    public static final String[] PRODUCT_PRICE = { "xpath://h3[@class='price-container']" };

    // --- CART PAGE ---
    public static final String[] PLACE_ORDER_BTN = { "xpath://button[text()='Place Order']" };
    public static final String[] CART_PRODUCT_NAME = { "xpath://td[text()='%s']" };
    public static final String[] DELETE_PRODUCT = { "xpath://td[text()='%s']/following-sibling::td/a[text()='Delete']" };
}