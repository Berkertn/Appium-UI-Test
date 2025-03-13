package org.mobile.pages;

import io.appium.java_client.AppiumBy;
import org.mobile.base.BasePage;
import org.mobile.base.DriverManager;
import org.mobile.base.DriverManager.OS_TYPES;
import org.openqa.selenium.By;

public class HomePage extends BasePage {
    By locationButtonAnd = AppiumBy.xpath("//android.view.View[@resource-id=\"location_button\"]");
    By dailyForecastButtonAnd = AppiumBy.xpath("//android.widget.FrameLayout[@content-desc=\"Daily\"]");
    By locatorDayTextsAnd = AppiumBy.xpath("//android.widget.TextView[@resource-id=\"com.accuweather.android:id/date\"]");
    By locatorDaysTextWithDynamicFieldAnd = AppiumBy.xpath("//android.widget.TextView[@resource-id=\"com.accuweather.android:id/date\" and @text=\"%s\"]");

    public HomePage() {
        locators.addLocator("locationButton", OS_TYPES.android, locationButtonAnd);
        locators.addLocator("dailyForecastButton", OS_TYPES.android, dailyForecastButtonAnd);
        locators.addLocator("dayTexts", DriverManager.OS_TYPES.android, locatorDayTextsAnd);
        locators.addLocator("dayTextDynamic", DriverManager.OS_TYPES.android, locatorDaysTextWithDynamicFieldAnd);
    }
}
