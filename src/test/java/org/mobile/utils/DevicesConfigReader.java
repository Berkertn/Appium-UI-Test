package org.mobile.utils;

import lombok.Getter;
import org.mobile.config.DeviceConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import static org.mobile.base.DriverManager.parsePlatform;
import static org.mobile.base.ThreadLocalManager.RUN_ON_LOCAL;
import static org.mobile.base.ThreadLocalManager.getOSPlatform;
import static org.mobile.config.LogConfig.logInfo;

public class DevicesConfigReader {
    @Getter
    private static final List<DeviceConfig> deviceConfigs = new ArrayList<>();

    static {
        try {
            logInfo("Loading device configs");
            List<Path> configFiles = getDeviceConfigFiles();
            for (Path configFile : configFiles) {
                Properties properties = new Properties();
                try (InputStream input = Files.newInputStream(configFile)) {
                    properties.load(input);

                    DeviceConfig deviceConfig = new DeviceConfig(
                            properties.getProperty("deviceName"),
                            properties.getProperty("deviceUDID"),
                            parsePlatform(properties.getProperty("platform")),
                            Integer.parseInt(properties.getProperty("port")),
                            Boolean.parseBoolean(properties.getProperty("isRealDevice")),
                            Boolean.parseBoolean(properties.getProperty("isOnUse"))
                    );
                    //device config list is filtered based on the platform and isOnUse flag
                    if (deviceConfig.getPlatform() == getOSPlatform() && deviceConfig.getIsOnUse()) {
                        deviceConfigs.add(deviceConfig);
                    }
                }
            }
            logInfo("Device configs loaded successfully:\n [%s]\n".formatted(deviceConfigs));
            if (deviceConfigs.isEmpty()) {
                throw new RuntimeException("No available devices found in configuration!");
            }
            // Sorts list based on port numbers (smallest to largest).
            // This ensures that when selecting a device (e.g., findFirst()), the one with the lowest port number is chosen first.
            deviceConfigs.sort(Comparator.comparingInt(DeviceConfig::getPort));
        } catch (IOException e) {
            throw new RuntimeException("Error loading device config properties!", e);
        }
    }

    private static List<Path> getDeviceConfigFiles() throws IOException {
        String fileSuffix = RUN_ON_LOCAL ? "*.local.device.properties" : "*.docker.device.properties";
        logInfo("Searching for device configuration files with suffix: " + fileSuffix);
        List<Path> configFiles = new ArrayList<>();
        URL resource = DevicesConfigReader.class.getClassLoader().getResource("devices");
        if (resource != null) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(resource.toURI()), fileSuffix)) {
                stream.forEach(configFiles::add);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }

        if (configFiles.isEmpty()) {
            Path configDir = Paths.get("src/main/resources/devices");
            if (Files.exists(configDir)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(configDir, fileSuffix)) {
                    stream.forEach(configFiles::add);
                }
            }
        }

        if (configFiles.isEmpty()) {
            throw new RuntimeException("No device configuration files found in resources/devices directory!");
        }
        return configFiles;
    }
}