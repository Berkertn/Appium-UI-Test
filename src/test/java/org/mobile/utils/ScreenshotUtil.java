package org.mobile.utils;

import org.apache.commons.exec.ExecuteException;
import org.mobile.base.DriverManager;
import org.openqa.selenium.OutputType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mobile.base.ThreadLocalManager.testStartDate;
import static org.mobile.config.LogConfig.logDebug;
import static org.mobile.config.LogConfig.logError;

public class ScreenshotUtil {
    private static final String SCREENSHOT_PATH = ConfigReader.get("ssPath");

    public static String captureScreenshot(String testName) {
        testName = testName.replaceAll("\\s+", "_");
        try {
            String fileName = SCREENSHOT_PATH + testName + "_" + testStartDate + ".png";
            File screenshotDir = new File(SCREENSHOT_PATH);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }
            File screenshot = DriverManager.getDriver().getScreenshotAs(OutputType.FILE);
            logDebug("Screenshot captured for test name of [%s] \nwith absolute path: [%s]".formatted(testName, screenshot.getAbsolutePath()));
            Files.copy(screenshot.toPath(), Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (Exception e) {
            logError("!!Screenshot could not be captured, Error: %s\n".formatted(e.getMessage()));
            return null;
        }
    }
}
