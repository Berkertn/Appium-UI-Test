package org.mobile.pages;

import org.mobile.base.PageManager;

import static org.mobile.config.LogConfig.logInfo;

public class PageMapper {
    public static void mapPages() {
        PageManager.registerPage("HomePage", HomePage.class);
        PageManager.registerPage("LocationPage", LocationPage.class);
        PageManager.registerPage("ForecastDayDetailPage", ForecastDayDetailPage.class);
        logInfo("Page Mapping Completed");
    }
}
