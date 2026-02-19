package com.irfan.ecommerce.util;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.io.File;

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
        // WALMART-SCALE FIX: Explicitly define the report directory relative to the project root
        String rootPath = System.getProperty("user.dir");
        File directory = new File(rootPath + "/reports");
        
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 1. Dynamic naming for local historical archive
        String timestampedFile = rootPath + "/reports/ExecutionReport_" + System.currentTimeMillis() + ".html";
        // 2. Static naming (index.html) for reliable GitHub Actions artifact mapping
        String latestFile = rootPath + "/reports/index.html";

        ExtentSparkReporter timestampedReporter = new ExtentSparkReporter(timestampedFile);
        ExtentSparkReporter latestReporter = new ExtentSparkReporter(latestFile);

        // Standardizing the enterprise "Pro" dashboard look
        latestReporter.config().setTheme(Theme.DARK); 
        latestReporter.config().setDocumentTitle("Walmart-Scale Quality Audit");
        latestReporter.config().setEncoding("utf-8");
        latestReporter.config().setReportName("Demoblaze Core Regression");

        extent = new ExtentReports();
        // Attach both: ensures CI finds index.html while you keep your history
        extent.attachReporter(latestReporter, timestampedReporter);
        
        extent.setSystemInfo("Quality Architect", "Irfan Muneer");
        extent.setSystemInfo("Platform", System.getProperty("os.name"));
        extent.setSystemInfo("Environment", "Staging-Cloud");

        return extent;
    }
}
