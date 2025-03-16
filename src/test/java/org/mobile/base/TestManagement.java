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
import static org.mobile.utils.AppiumUtil.isAppRunning;
import static org.mobile.utils.FileUtil.cleanTestOutputFolder;

@ExtendWith(TestResultLogger.class)
public class TestManagement extends StepDefinitionBase {

    protected AppiumDriver driver;
    public static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    public static void projectSetUp() {
        cleanTestOutputFolder();
        logInfo("\033[32m[Before-All-Thread-[%s]]-Configs setting:\033[0m\n".formatted(Thread.currentThread().getName()));
        TestExecutionConfig.initialize();
        ThreadLocalManager.osPlatformTL.set(parsePlatform(ConfigReader.get("platform")));
        logInfo("\033[32m[Before-All-Thread-[%s]]-Tests Starting\033[0m\n".formatted(Thread.currentThread().getName()));

        if (ThreadLocalManager.getIsParallelEnabled()) {
            logInfo("\033[32m[Before-All-Thread-[%s]]-Tests will run parallel. Starting all Appium servers\033[0m".formatted(Thread.currentThread().getName()));
            DevicesConfigReader.getDeviceConfigs().forEach(AppiumServerManager::startServer);
        } else {
            logInfo("\033[32mTests will run single-thread. Starting the Appium server\033[0m");
            DevicesConfigReader.getDeviceConfigs().stream().findFirst().ifPresent(AppiumServerManager::startServer);
        }
    }

    @BeforeEach
    public void setUpTestCase(TestInfo testInfo) {
      /*  if (!isAppRunning("com.accuweather.android")) {
            LogConfig.logInfo("App is NOT running. Launching the app!");
        } else {
            LogConfig.logInfo("App is already running!");
        }*/

        logInfo("\033[34m[Before-Each-Thread-[%s]]-Starting test: [%s]\033[0m".formatted(Thread.currentThread().getName(), testInfo.getDisplayName()));

        logInfo("Test running on thread: " + Thread.currentThread().getName());
        driver = DriverManager.getDriver();
        ExtentReportManager.startTest(testInfo.getDisplayName());

        logInfo("Test running on thread: [%s] and port: [%d] ".formatted(Thread.currentThread().getName(), getDeviceConfig().getPort()));
    }

    @AfterEach
    public void tearDownTestCase(TestInfo testInfo) {
        logInfo("[After-Each-Thread-[%s]]-Ending test: [%s]".formatted(Thread.currentThread().getName(), testInfo.getDisplayName()));
        //String screenshotPath = ScreenshotUtil.captureScreenshot(testInfo.getDisplayName());
        //TODO driver will exit in TestResultLogger because afterEach run before testFailed trigger

    /*    SS_PATH.set(screenshotPath);
        logDebug("[After-Each-Thread-[%s]]--Screen shoot captured and saved to: [%s]".formatted(Thread.currentThread().getName(), screenshotPath));
        DeviceManager.releaseDevice();
        logDebug("[After-Each-Thread-[%s]]-Device released in Thread".formatted(Thread.currentThread().getName()));
        DriverManager.quitDriver();
        logDebug("[After-Each-Thread-[%s]]-Driver quited in Thread".formatted(Thread.currentThread().getName()));*/
    }

    @AfterAll
    public static void projectTearDown() {
        logInfo("\033[31mAll tests completed. Stopping all Appium servers...\033[0m");
        DevicesConfigReader.getDeviceConfigs().forEach(AppiumServerManager::stopServer);
        logInfo("\033[31mAll Appium servers have been stopped.\033[0m");
    }
}
