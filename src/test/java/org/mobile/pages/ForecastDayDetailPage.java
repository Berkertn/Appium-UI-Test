package org.mobile.pages;

import io.appium.java_client.AppiumBy;
import org.mobile.base.BasePage;
import org.mobile.base.DriverManager.OS_TYPES;
import org.openqa.selenium.By;

public class ForecastDayDetailPage extends BasePage {
    By dateLocatorInHeaderAnd = AppiumBy.xpath("//android.widget.TextView[@resource-id=\"com.accuweather.android:id/time_header\"]");
    By highestAndLowestTempLocatorAnd = AppiumBy.xpath("//android.widget.TextView[@resource-id=\"com.accuweather.android:id/max_temperature\"]");
    By weatherIconPhraseLocatorAnd = AppiumBy.xpath("(//android.view.ViewGroup[@resource-id=\"com.accuweather.android:id/temperature_block\"]//android.widget.ImageView)[1]");
    By dayTabLocatorAnd = AppiumBy.xpath("(//android.widget.HorizontalScrollView[@resource-id='com.accuweather.android:id/tab_layout']//android.widget.LinearLayout//android.widget.TextView)[1]");
    By nightTabLocatorAnd = AppiumBy.xpath("(//android.widget.HorizontalScrollView[@resource-id='com.accuweather.android:id/tab_layout']//android.widget.LinearLayout//android.widget.TextView)[2]");

    public ForecastDayDetailPage() {
        locators.addLocator("dateHeaderText", OS_TYPES.android, dateLocatorInHeaderAnd);
        locators.addLocator("highestAndLowestTemperatureText", OS_TYPES.android, highestAndLowestTempLocatorAnd);
        locators.addLocator("weatherPhareIcon", OS_TYPES.android, weatherIconPhraseLocatorAnd);
        locators.addLocator("dayTabButton", OS_TYPES.android, dayTabLocatorAnd);
        locators.addLocator("nightTabButton", OS_TYPES.android, nightTabLocatorAnd);
    }
}
