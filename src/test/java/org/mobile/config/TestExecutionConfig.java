package org.mobile.config;

import org.mobile.base.ThreadLocalManager;

import static org.mobile.config.LogConfig.logInfo;

public class TestExecutionConfig {

    static {
        System.setProperty("junit.jupiter.execution.parallel.enabled", "false");
        String parallelEnabled = System.getProperty("junit.jupiter.execution.parallel.enabled");

        if (Boolean.parseBoolean(parallelEnabled)) {
            ThreadLocalManager.setIsParallelEnabled(true);
        } else {
            System.setProperty("junit.jupiter.execution.parallel.config.fixed.parallelism", "1");

        }
    }

    public static void initialize() {
        logInfo("Parallel Enabled: " + System.getProperty("junit.jupiter.execution.parallel.enabled"));
        logInfo("Parallel Mode Default: " + System.getProperty("junit.jupiter.execution.parallel.mode.default"));
        logInfo("Parallel Mode Classes: " + System.getProperty("junit.jupiter.execution.parallel.mode.classes.default"));
        logInfo("Parallelism Level: " + System.getProperty("junit.jupiter.execution.parallel.config.fixed.parallelism"));
    }
}

