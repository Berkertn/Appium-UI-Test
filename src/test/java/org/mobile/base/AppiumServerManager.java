package org.mobile.base;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.mobile.config.DeviceConfig;
import org.mobile.utils.ConfigReader;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.mobile.base.ThreadLocalManager.RUN_ON_LOCAL;
import static org.mobile.config.LogConfig.logInfo;

public class AppiumServerManager {

    private static final Map<Integer, AppiumDriverLocalService> serviceMap = new ConcurrentHashMap<>();

    public static void startServer(DeviceConfig deviceConfig) {
        if (serviceMap.containsKey(deviceConfig.getPort())) {
            return;
        }

        try {
            String pathForNodeOfAppium = RUN_ON_LOCAL ? ConfigReader.get("appium_global_node_path_local") : ConfigReader.get("appium_global_node_path");
            AppiumDriverLocalService service = new AppiumServiceBuilder()
                    .withAppiumJS(new File(pathForNodeOfAppium))
                    .usingPort(deviceConfig.getPort())
                    .build();
            service.start();
            serviceMap.put(deviceConfig.getPort(), service);
            logInfo("\033[32m[Thread-%s]Appium Server Started on port =[%d]!\033[0m".formatted(Thread.currentThread().getName(),deviceConfig.getPort()));
        } catch (Exception e) {
            throw new RuntimeException("Appium Server Start Failed!\n\n" + e.getMessage());
        }
    }

    public static void stopServer(DeviceConfig config) {
        AppiumDriverLocalService service = serviceMap.get(config.getPort());
        if (service != null) {
            service.stop();
            logInfo("\033[31m[Thread-%s]Appium Server Stopped in port: [%s]!\033[0m".formatted(Thread.currentThread().getName(),config.getPort()));
        }
    }
}

