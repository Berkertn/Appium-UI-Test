package org.mobile.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.api.models.forecast.fiveDaily.DailyForecastResponse;
import org.api.models.forecast.fiveDaily.DailyForecast;
import org.junit.jupiter.api.*;
import org.mobile.base.TestManagement;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.api.steps.RequestSteps.*;
import static org.mobile.config.LogConfig.logDebug;
import static org.mobile.utils.DateUtil.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WeatherAppTest extends TestManagement {

    @Test
    @Order(1)
    @Tag("wip")
    @DisplayName("Weather Five Day Forecast UI and API Test")
    void weatherFiveDayForecastUIAndAPITest() throws JsonProcessingException {
        String city = "Istanbul";
        String key = getLocationKeyFor(city);
        String dailyForecasts = getResponseForFiveDaysForecast(key);
        DailyForecastResponse dailyForecastResponse = objectMapper.readValue(dailyForecasts, DailyForecastResponse.class);
        DailyForecast hottestDate = findHottestDate(dailyForecastResponse);
        String hottestDayAsUIFormat = convertDateToUIFormat(convertDateToLocalDate(hottestDate.getDate()));
        String hottestDay = String.valueOf(getDayAsNumber(convertDateToLocalDate(hottestDate.getDate())));
        logDebug("Hottest date found as: " + hottestDay);

        // UI TESTS
        iSetThePageAsFrom("HomePage", "/");
        iTapOnElement("locationButton");

        iSetThePageAsFrom("LocationPage", "/");
        iTapOnElement("locationSearchText");
        iWriteIntoElement("locationSearchText", city);
        iWaitToBeVisible("locationResultFirst");
        iTapOnElement("locationResultFirst");
        iSetThePageAsFrom("HomePage", "/");
        iTapOnElement("dailyForecastButton");

        List<WebElement> elements = iGetElements("dayTexts");
        for (WebElement element : elements) {
            String text = element.getText();
            if (text.contains(hottestDay)) {
                element.click();
                break;
            }
        }
        iSetThePageAsFrom("ForecastDayDetailPage", "/");
        List<String> locatorKeysToSee = List.of(
                "dateHeaderText",
                "highestAndLowestTemperatureText",
                "weatherPhareIcon",
                "dayTabButton",
                "nightTabButton"
        );
        /// Day tab UI elements and their value check
        iVerifyToElements(locatorKeysToSee);
        iVerifyTextInElement("dateHeaderText", hottestDayAsUIFormat);
        iVerifyTextInElement("highestAndLowestTemperatureText", String.valueOf(hottestDate.getTemperature().getMaximum().getValue()));
        iVerifyTextInElement("weatherPhareIcon", hottestDate.getDay().getIconPhrase(), "content-desc");

        ///Change the weather day to night tab
        iTapOnElement("nightTabButton");
        /// Night tab UI elements and their value check
        iVerifyToElements(locatorKeysToSee);
        iVerifyTextInElement("dateHeaderText", hottestDayAsUIFormat);
        iVerifyTextInElement("highestAndLowestTemperatureText", String.valueOf(hottestDate.getTemperature().getMinimum().getValue()));
        iVerifyTextInElement("weatherPhareIcon", hottestDate.getNight().getIconPhrase(), "content-desc");
    }

}