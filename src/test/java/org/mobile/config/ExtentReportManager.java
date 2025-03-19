package org.mobile.config;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.mobile.utils.ConfigReader;

public class ExtentReportManager {
    private static final ExtentReports extent = new ExtentReports();
    private static final ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();
    private static boolean isReporterConfigured = false;

    private ExtentReportManager() {
    }

    public static synchronized void configureReporter() {
        if (!isReporterConfigured) {
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(ConfigReader.get("reportPath"));
            extent.attachReporter(sparkReporter);
            isReporterConfigured = true;
        }
    }

    public static void startTest(String testName) {
        configureReporter();
        ExtentTest extentTest = extent.createTest(testName);
        testThreadLocal.set(extentTest);
    }

    public static ExtentTest getTest() {
        return testThreadLocal.get();
    }

    public static void endTest() {
        ExtentTest test = testThreadLocal.get();
        if (test != null) {
            test.info("[Thread-%s]Ending test: %s".formatted(Thread.currentThread().getName(), test.getModel().getName()));
            testThreadLocal.remove();
        }
    }

    public static synchronized void flushReports() {
        extent.flush();
    }
}
