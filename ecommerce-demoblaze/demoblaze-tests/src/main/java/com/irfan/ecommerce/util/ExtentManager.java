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
 * overwriting each other. I'd lose the failure proof from the previous run.
 * Also, parallel runs were making the report data look like a jumbled mess.
 * - WHAT I DID: I added timestamps so every run has its own file and made the
 * instance thread-safe so it doesn't get confused when 10 tests report at once.
 * - THE RESULT: We have a clean history of every single run. If something
 * breaks at 3 AM in the CI, the proof is right there waiting for me.
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
        // We use an absolute path provided by the CI, or fallback to local project root
        String reportDir = System.getProperty("report.dir", System.getProperty("user.dir") + "/reports");
        File directory = new File(reportDir);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Use the directory object to build the file paths
        File latestFile = new File(directory, "index.html");
        File timestampedFile = new File(directory, "ExecutionReport_" + System.currentTimeMillis() + ".html");

        ExtentSparkReporter latestReporter = new ExtentSparkReporter(latestFile);
        ExtentSparkReporter timestampedReporter = new ExtentSparkReporter(timestampedFile);
        
        extent = new ExtentReports();
        extent.attachReporter(latestReporter, timestampedReporter);
        return extent;
    }
}

