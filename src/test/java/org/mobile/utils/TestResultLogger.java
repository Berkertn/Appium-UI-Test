package org.mobile.utils;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.mobile.base.DeviceManager;
import org.mobile.base.DriverManager;
import org.mobile.config.ExtentReportManager;
import org.openqa.selenium.NoSuchSessionException;

import java.util.Optional;

import static org.mobile.config.LogConfig.logDebug;
import static org.mobile.config.LogConfig.logInfo;

public class TestResultLogger implements TestWatcher {


    @Override
    public void testSuccessful(ExtensionContext context) {
        logInfo("Test Passed: " + context.getDisplayName());
        ExtentReportManager.getTest().pass("Test Passed: " + context.getDisplayName());
        terminateDriver();
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName();
        logInfo("[Thread-%s][TestResultLogger]-Test Failed: %s - \nError: ".formatted(Thread.currentThread().getId(), testName) + cause.getMessage());
        String screenshotPath = ScreenshotUtil.captureScreenshot(context.getDisplayName());
        if (screenshotPath == null) {
            ExtentReportManager.getTest().fail("Test Failed: %s - \nError: ".formatted(context.getDisplayName()) + cause.getMessage());
        } else {
            ExtentReportManager.getTest().fail("[TestResultLogger-Thread-%s]-Test Failed: %s - \nError: ".formatted(Thread.currentThread().getId(), context.getDisplayName()) + cause.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath("../../" + screenshotPath).build());

        }
        terminateDriver();
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        logInfo("[Thread-%s]-Test Aborted: %s - Reason: %s"
                .formatted(Thread.currentThread().getId(), context.getDisplayName(), cause.getMessage()));
        ExtentReportManager.getTest().fail("[Thread-%s]-Test Aborted: %s - Reason: " + cause.getMessage());
        ExtentReportManager.endTest();
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        logInfo("[Thread-%s]-Test Disabled: %s - Reason: %s"
                .formatted(Thread.currentThread().getId(), context.getDisplayName(), reason.orElse("Unknown")));
        ExtentReportManager.endTest();
    }

    private void terminateDriver() {
        ExtentReportManager.endTest();
        DeviceManager.releaseDevice();
        logDebug("Device released in Thread: [%s]".formatted(Thread.currentThread().getName()));
        try{
            DriverManager.quitDriver();
        }catch (NoSuchSessionException e){
            logDebug("Driver already quited in Thread: [%s]".formatted(Thread.currentThread().getName()));
        }
        logDebug("Driver quited in Thread: [%s]".formatted(Thread.currentThread().getName()));
    }
}
