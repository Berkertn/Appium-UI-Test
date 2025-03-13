package org.mobile.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mobile.commons.StepDefinitionBase;
import org.mobile.config.ExtentReportManager;
import org.mobile.config.TestExecutionConfig;
import org.mobile.utils.ConfigReader;
import org.mobile.utils.DevicesConfigReader;
import org.mobile.utils.TestResultLogger;
import org.mobile.config.LogConfig;

import static org.mobile.base.DriverManager.getDeviceConfig;
import static org.mobile.base.DriverManager.parsePlatform;
import static org.mobile.config.LogConfig.logInfo;
import static org.mobile.pages.PageMapper.mapPages;
import static org.mobile.utils.AppiumUtil.isAppRunning;

@ExtendWith(TestResultLogger.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestManagement extends StepDefinitionBase {

    protected AppiumDriver driver;
    public static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void projectSetUp() {
        logInfo("Page Mapping Starting");
        mapPages();
        logInfo("Configs setting:");
        TestExecutionConfig.initialize();
        ThreadLocalManager.osPlatformTL.set(parsePlatform(ConfigReader.get("platform")));
        logInfo("Tests Starting...");
        if (ThreadLocalManager.getIsParallelEnabled()) {
            logInfo("Tests will run parallel. Starting all Appium servers...");
            DevicesConfigReader.getDeviceConfigs().forEach(AppiumServerManager::startServer);
        } else {
            logInfo("Tests will run single-thread. Starting the Appium server...");
            DevicesConfigReader.getDeviceConfigs().stream().findFirst().ifPresent(AppiumServerManager::startServer);
        }
    }

    @BeforeEach
    public void setUpTestCase(TestInfo testInfo) {
        if (!isAppRunning("com.accuweather.android")) {
            LogConfig.logInfo("App is NOT running. Launching the app...");

        } else {
            LogConfig.logInfo("App is already running.");
        }
        logInfo("Starting test: " + testInfo.getDisplayName());
        logInfo("Test running on thread: " + Thread.currentThread().getName());
        driver = DriverManager.getDriver();
        ExtentReportManager.startTest(testInfo.getDisplayName());
        logInfo("Test running on thread: [%s] and port: [%d] ".formatted(Thread.currentThread().getName(), getDeviceConfig().getPort()));
    }

    @AfterEach
    public void tearDownTestCase(TestInfo testInfo) {
        logInfo("Ending test: [%s]".formatted(testInfo.getDisplayName()));
        //TODO driver will exit in TestResultLogger because afterEach run before testFailed trigger
        /*DeviceManager.releaseDevice();
        logDebug("Device released in Thread: [%s]".formatted(Thread.currentThread().getName()));
        DriverManager.quitDriver();
        logDebug("Driver quited in Thread: [%s]".formatted(Thread.currentThread().getName()));*/
    }


    @AfterAll
    public static void projectTearDown() {
        logInfo("All tests completed. Stopping all Appium servers...");
        DevicesConfigReader.getDeviceConfigs().forEach(AppiumServerManager::stopServer);
        logInfo("\033[31mAll Appium servers have been stopped.\033[0m");
    }
}
