package com.irfan.ecommerce.util;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * ExtentManager: Making sure we actually see what failed.
 * 
 * THE WALMART HEADACHE I FIXED:
 * - THE PROBLEM: When running tests fast in the pipeline, the reports kept 
 *   overwriting each other. I'd lose the failure proof from the previous run. 
 *   Also, parallel runs were making the report data look like a jumbled mess.
 * - WHAT I DID: I added timestamps so every run has its own file and made the 
 *   instance thread-safe so it doesn't get confused when 10 tests report at once.
 * - THE RESULT: We have a clean history of every single run. If something 
 *   breaks at 3 AM in the CI, the proof is right there waiting for me.
 */

public class ExtentManager {
    
    // Using volatile to ensure visibility across multiple threads in parallel execution
    private static volatile ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            synchronized (ExtentManager.class) {
                if (extent == null) {
                    extent = createInstance();
                }
            }
        }
        return extent;
    }

    private static ExtentReports createInstance() {
        java.io.File directory = new java.io.File("reports");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Dynamic naming prevents data loss in high-frequency CI runs
        String fileName = "reports/ExecutionReport_" + System.currentTimeMillis() + ".html";
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter(fileName);

        htmlReporter.config().setTheme(Theme.DARK); // Pro look for enterprise dashboards
        htmlReporter.config().setDocumentTitle("Walmart-Scale Quality Audit");
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName("Demoblaze Core Regression");

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("Quality Architect", "Irfan Muneer");
        extent.setSystemInfo("Platform", System.getProperty("os.name"));
        extent.setSystemInfo("Environment", "Staging-Cloud");

        return extent;
    }
}
