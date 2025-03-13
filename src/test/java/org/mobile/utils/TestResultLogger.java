package org.mobile.utils;

import com.aventstack.extentreports.MediaEntityBuilder;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.mobile.base.DeviceManager;
import org.mobile.base.DriverManager;
import org.mobile.config.ExtentReportManager;

import java.util.Optional;

import static org.mobile.config.LogConfig.logDebug;
import static org.mobile.config.LogConfig.logInfo;

public class TestResultLogger implements TestWatcher {


    @Override
    public void testSuccessful(ExtensionContext context) {
        logInfo("Test Passed: " + context.getDisplayName());
        terminateDriver();
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName();
        logInfo("[Thread-%s][TestResultLogger]-Test Failed: %s - Error: ".formatted(Thread.currentThread().getId(), context.getDisplayName()) + cause.getMessage());

        //TODO testFailed runs after afterEach method, so we need find a solution for that
        String screenshotPath = ScreenshotUtil.captureScreenshot(testName);
        logDebug("[Thread-%s]-Screen shoot captured and saved to: [%s]".formatted(Thread.currentThread().getId(), screenshotPath));

        ExtentReportManager.getTest().fail("[Thread-%s]-Test Failed: %s - \nError: ".formatted(Thread.currentThread().getId(), context.getDisplayName()) + cause.getMessage(),
                MediaEntityBuilder.createScreenCaptureFromPath("../../" + screenshotPath).build());
        ExtentReportManager.endTest();
        terminateDriver();
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        logInfo("[Thread-%s]-Test Aborted: %s - Reason: %s"
                .formatted(Thread.currentThread().getId(), context.getDisplayName(), cause.getMessage()));
        terminateDriver();
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        logInfo("[Thread-%s]-Test Disabled: %s - Reason: %s"
                .formatted(Thread.currentThread().getId(), context.getDisplayName(), reason.orElse("Unknown")));
        terminateDriver();
    }

    private void terminateDriver() {
        DeviceManager.releaseDevice();
        logDebug("Device released in Thread: [%s]".formatted(Thread.currentThread().getName()));
        DriverManager.quitDriver();
        logDebug("Driver quited in Thread: [%s]".formatted(Thread.currentThread().getName()));
    }
}
