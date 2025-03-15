package org.mobile.pages;

import io.appium.java_client.AppiumBy;
import org.mobile.base.BasePage;
import org.mobile.base.DriverManager.OS_TYPES;
import org.openqa.selenium.By;

public class CurrentConditionsPage extends BasePage {
    By pageHeaderLocatorAnd = AppiumBy.xpath("(//android.widget.TextView[@text=\"Current Conditions\"])[1]");
    By weatherIconPhareDynamicLocatorAnd = AppiumBy.androidUIAutomator("new UiSelector().text(\"%s\")");
    By unitDynamicLocatorAnd = AppiumBy.androidUIAutomator("new UiSelector().text(\"%s\")");
    By weatherIconPhareImageLocatorAnd = AppiumBy.className("android.widget.ImageView");
    By currentTempDynamicLocatorAnd = AppiumBy.xpath("//android.widget.TextView[@text=\"%s\"]");

    public CurrentConditionsPage() {
        locators.addLocator("pageHeaderText", OS_TYPES.android, pageHeaderLocatorAnd);
        locators.addLocator("weatherIconPhareDynamicText", OS_TYPES.android, weatherIconPhareDynamicLocatorAnd);
        locators.addLocator("weatherIconPhareImage", OS_TYPES.android, weatherIconPhareImageLocatorAnd);
        locators.addLocator("currentTempDynamicText", OS_TYPES.android, currentTempDynamicLocatorAnd);
        locators.addLocator("unitDynamicText", OS_TYPES.android, unitDynamicLocatorAnd);
    }
}
