package org.mobile.utils.appium;

import com.google.common.collect.ImmutableMap;
import org.mobile.base.DriverManager;

public class AppiumUtil {

    public static boolean isAppRunning(String packageName) {
        try {
            String output = DriverManager.getDriver().executeScript("mobile: shell", ImmutableMap.of(
                    "command", "pidof " + packageName
            )).toString();

            return output != null && !output.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

}
