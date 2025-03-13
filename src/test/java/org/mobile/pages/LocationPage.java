package org.mobile.pages;

import io.appium.java_client.AppiumBy;
import org.mobile.base.BasePage;
import org.mobile.base.DriverManager;
import org.openqa.selenium.By;

public class LocationPage extends BasePage {

    By locationSearchLocatorAnd = AppiumBy.xpath("//android.widget.EditText");
    By locatorResulFirstAnd = AppiumBy.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View[2]/android.view.View[1]");


    public LocationPage() {
        locators.addLocator("locationSearchText", DriverManager.OS_TYPES.android, locationSearchLocatorAnd);
        locators.addLocator("locationResultFirst", DriverManager.OS_TYPES.android, locatorResulFirstAnd);
    }
}