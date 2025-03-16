package org.mobile.utils;

import org.mobile.base.DriverManager;
import org.openqa.selenium.OutputType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mobile.config.LogConfig.logDebug;

public class ScreenshotUtil {
    private static final String SCREENSHOT_PATH = ConfigReader.get("ssPath");

    public static String captureScreenshot(String testName) {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = SCREENSHOT_PATH + testName + "_" + timestamp + ".png";
            File screenshotDir = new File(SCREENSHOT_PATH);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }
            File screenshot = DriverManager.getDriver().getScreenshotAs(OutputType.FILE);
            logDebug("Screenshot captured for test name of [%s] \nwith absolute path: [%s]".formatted(testName, screenshot.getAbsolutePath()));
            Files.copy(screenshot.toPath(), Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while taking screenshot: \n", e);
        }
    }
}
