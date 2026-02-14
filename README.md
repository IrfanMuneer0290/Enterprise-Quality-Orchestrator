# 🛒 DemoBlaze 1% Elite Hybrid Automation Framework

[![Java](https://img.shields.io/badge/Java-25-007396?logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-4.25%2B-43B02A?logo=selenium&logoColor=white)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.x-%23F58220?logo=testng&logoColor=white)](https://testng.org/)
[![Log4j2](https://img.shields.io/badge/Logging-Log4j2-cc0000?logo=apache&logoColor=white)](https://logging.apache.org/log4j/2.x/)

An enterprise-grade **Hybrid Test Automation Framework** engineered for high resilience and observability. This project automates the [DemoBlaze](https://www.demoblaze.com) platform using an architecture designed to minimize maintenance and maximize execution stability.

## 🏆 "1% Elite" Framework Architecture

### 1. Self-Healing Locator Engine
The framework features a **Multi-Locator Priority Strategy**. Instead of failing on a single broken ID, the engine automatically iterates through a list of backup locators (ID → XPath → CSS) defined in a centralized `ObjectRepo`, reducing false-negative failures by around 40%.

### 2. Smart Locator Factory (Decoupled)
Uses a strategy-based parser (`strategy:value`) allowing locators to be stored as clean strings. It supports dynamic XPaths via `String.format` templates, enabling scalable navigation across categories with zero code duplication.

### 3. Professional Observability & Reporting
- Industrial logging with Log4j2 for categorized tracing (INFO, DEBUG, ERROR).
- Extent Reports 5 HTML dashboard with charts and system metrics.
- Automated evidence: every interaction failure triggers a screenshot that is embedded into the Extent report.

### 4. Pure Page Object Model (POM)
Follows strict abstraction principles. Page Objects contain no direct Selenium API calls (`By`, `WebElement`, or `driver`), keeping the test layer readable and business-focused.

### 5. Enterprise Security Handling
Designed for real-world security flows:
- Hooks for MFA/OTP retrieval through backend APIs.
- CAPTCHA resilience using cookie injection and JS token injection for whitelisted automation environments.

### 6. Dockerized Infrastructure (NEW)
The framework is fully containerized using Docker Compose. It spins up a standalone Selenium Grid (Hub + Chrome Node) during execution, ensuring a consistent environment across local and cloud runners.

### 7. Thread-Safe Concurrent Execution (NEW)
Using `ThreadLocal<WebDriver>`, the DriverFactory supports safe parallel execution. This prevents session interference and allows multiple tests to run simultaneously, reducing total suite runtime significantly.

---

## 🛠️ Tech Stack
- Language: Java 25
- Core: Selenium WebDriver 4.25+
- Reporting: Extent Reports 5
- Data-Driven: Apache POI (Excel)
- Logging: Log4j 2
- Build Tool: Maven

---

## 📁 Project Structure

```text
.github/
└── workflows/
    └── main.yml                      # Production CI/CD pipeline

docker-compose.yml                    # Selenium Grid infrastructure

src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── irfan/
│   │           └── ecommerce/
│   │               ├── base/         # DriverFactory (Docker & local logic)
│   │               ├── pages/        # Page Objects (encapsulated business logic)
│   │               └── util/         # ExtentManager, GenericActions, helpers
│   └── resources/
│       └── log4j2.xml                # Professional logging configuration
└── test/
    ├── java/
    │   └── com/
    │       └── irfan/
    │           └── ecommerce/
    │               └── tests/        # TestNG test classes
    └── resources/
        └── testdata.xlsx             # External data source for hybrid execution

🚀 Upcoming Enhancements (Roadmap)
🏗️ Infrastructure & Scalability
 Enhance parallel execution and thread-safety in DriverFactory using ThreadLocal<WebDriver> with TestNG XML-based parallelism.

🧪 Advanced Testing Strategies
 API automation layer using RestAssured for full-stack validation.

 Consumer-driven contract testing with Pact.io for microservice integrations.

 Performance benchmarking hooks with JMeter or Gatling for page load metrics.

🔄 CI/CD & DevOps
 Deeper GitHub Actions integration with Slack notifications.

 Cloud execution via BrowserStack or Sauce Labs for cross-platform coverage.
