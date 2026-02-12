package com.irfan.ecommerce.util;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports createInstance() {
        String fileName = "reports/ExtentReport_" + System.currentTimeMillis() + ".html";
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter(fileName);
        
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setDocumentTitle("DemoBlaze Automation Report");
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName("Regression Suite Results");

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("Automation Engineer", "Irfan Muneer");
        extent.setSystemInfo("Environment", "QA/Stage");
        
        return extent;
    }
}
