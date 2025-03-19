package org.mobile.config;

import org.mobile.base.ThreadLocalManager;

import static org.mobile.config.LogConfig.logInfo;

public class TestExecutionConfig {

    static {
        String parallelEnabled = System.getProperty("junit.jupiter.execution.parallel.enabled", "true");

        if (Boolean.parseBoolean(parallelEnabled)) {
            ThreadLocalManager.setIsParallelEnabled(true);
        }
    }

    public static void initialize() {
        logInfo("TestExecutionConfig is set from junit-platform.properties file");
        logInfo("Parallel Enabled: " + System.getProperty("junit.jupiter.execution.parallel.enabled", "true"));
        logInfo("Parallel Mode Default: " + System.getProperty("junit.jupiter.execution.parallel.mode.default", "concurrent"));
        logInfo("Parallel Mode Classes: " + System.getProperty("junit.jupiter.execution.parallel.mode.classes.default", "concurrent"));
        logInfo("Parallelism Level: " + System.getProperty("junit.jupiter.execution.parallel.config.fixed.parallelism", "2"));
    }
}