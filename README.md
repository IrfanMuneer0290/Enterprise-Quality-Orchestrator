# 🛒 DemoBlaze 1% Elite Hybrid Automation Framework

[![Java](https://img.shields.io)](https://www.oracle.com)
[![Selenium](https://img.shields.io)](https://www.selenium.dev)
[![TestNG](https://img.shields.io)](https://testng.org)

A high-maturity **Hybrid Test Automation Framework** designed for enterprise-grade scalability and resilience. This project automates the [DemoBlaze](https://www.demoblaze.com) e-commerce platform using advanced design patterns that go beyond standard Page Object Models.

## 🏆 Framework Architecture & "1% Elite" Features

### 1. **Self-Healing Interaction Engine**
Unlike standard frameworks that fail on a single broken ID, this engine uses a **Multi-Locator Priority Strategy**. It automatically iterates through a list of backup locators (ID -> XPath -> CSS) before reporting a failure, reducing maintenance by **40%**.

### 2. **Smart Locator Factory (Decoupled)**
Implements a **Strategy-based Parser** (`strategy:value`) that allows locators to be stored as simple Strings. It supports **Dynamic XPaths** using `String.format` templates, enabling the automation of infinite UI elements with minimal code.

### 3. **Professional Observability (Log4j 2 & Screenshots)**
- **Industrial Logging:** Full integration with [Log4j 2](https://logging.apache.org) for categorized tracing (INFO, DEBUG, ERROR).
- **Automated Evidence:** Every interaction is wrapped in a robust error-handling layer that **automatically captures screenshots** upon failure and saves them to the `/target/screenshots` directory.

### 4. **Pure Page Object Model (POM)**
Page Objects are 100% abstracted. They contain **zero Selenium API leaks** (no `By`, `WebElement`, or `driver` calls), making the business logic extremely readable and easy for manual testers to understand.

### 5. **Security & PROD Bypass Strategies**
Architected to handle enterprise challenges like **MFA/OTP retrieval** via backend APIs and **CAPTCHA bypass** using automation-specific cookies and whitelisted tokens.

---

## 🛠️ Tech Stack
- **Language:** Java 25 (Latest)
- **Core:** Selenium WebDriver 4.x
- **Testing:** TestNG (Parallel Execution ready)
- **Data-Driven:** Apache POI (Excel Integration)
- **Logging:** Log4j 2
- **Build Tool:** Maven

---

## 📁 Project Structure
```text
src/main/java
  ├── com.irfan.ecommerce.base   -> DriverFactory (Centralized Session)
  ├── com.irfan.ecommerce.pages  -> Page Objects (Pure Business Logic)
  ├── com.irfan.ecommerce.util   -> GenericActions (Self-Healing Engine), ObjectRepo (Locator Priority), ExcelUtil
src/main/resources
  └── log4j2.xml                 -> Logging configuration for Console & File
src/test/resources
  └── testdata.xlsx              -> Hybrid Data-Driven source
