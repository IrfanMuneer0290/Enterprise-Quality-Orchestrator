# 🛒 DemoBlaze E-Commerce Automation Portfolio

A robust test automation framework built to demonstrate industry-standard practices in Web UI Automation. This project targets the [DemoBlaze](https://www.demoblaze.com) e-commerce platform using a scalable, maintainable architecture.

## 🚀 Tech Stack
*   **Language:** Java 25 (Latest JDK)
*   **Automation Tool:** [Selenium WebDriver 4.25+](https://www.selenium.dev)
*   **Test Runner:** [TestNG](https://testng.org)
*   **Build Tool:** [Apache Maven](https://maven.apache.org)
*   **Design Pattern:** Page Object Model (POM)
*   **Driver Management:** Selenium Manager (Built-in) & WebDriverManager

## 🛠️ Key Framework Features
*   **Centralized Driver Management:** Utilizes a `DriverFactory` for consistent browser lifecycle management and thread-safety.
*   **Decoupled Locators:** UI elements are managed via a `webelement.properties` file and a custom `CommonFunctionsUtil` to separate data from logic.
*   **Synchronization:** Implements **Explicit Waits** using `WebDriverWait` and `ExpectedConditions` to handle the asynchronous nature of the DemoBlaze SPA.
*   **Base Architecture:** A `BaseTest` class manages `@BeforeMethod` and `@AfterMethod` hooks to ensure a clean state for every test case.

## 📁 Project Structure
```text
src/main/java
  └── com.irfan.ecommerce
      ├── base    -> DriverFactory, BasePage
      ├── util    -> Property Readers, Wait Utilities
      └── pages   -> Page Objects (HomePage, etc.)
src/test/java
  └── com.irfan.ecommerce.tests -> Functional Test Suites
