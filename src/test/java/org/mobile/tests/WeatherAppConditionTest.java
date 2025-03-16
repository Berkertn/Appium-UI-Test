package org.mobile.tests;

import org.api.models.conditions.CurrentCondition;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mobile.base.TestHooks;

import java.util.List;

import static org.api.steps.RequestSteps.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Execution(ExecutionMode.CONCURRENT)
public class WeatherAppConditionTest extends TestHooks {

    @Test
    @Tag("condition")
    @Tag("smoke")
    @DisplayName("Weather Current Conditions For NY UI and API Test-2")
    void weatherConditionsUIAndAPITest() {
        String city = "New York";
        String key = getLocationKeyFor(city);
        CurrentCondition currentCondition = getFirstResponseOfCurrentConditionAsClassInstance(key);

        // UI TESTS
        iSetThePageAsFrom("HomePage", "/");
        iTapOnElement("locationButton");
        //city search
        iSetThePageAsFrom("LocationPage", "/");
        iTapOnElement("locationSearchText");
        iWriteIntoElement("locationSearchText", city);
        try {
            iWaitToBeVisible("locationResultFirst");
        } catch (Exception e) {
            iTapEnter();
        }
        iTapOnElement("locationResultFirst");

        iSetThePageAsFrom("HomePage", "/");
        iScrollToElement("currentConditionsTempText");
        iVerifyToElement("currentConditionsTempText");
        iVerifyToElement("currentConditionsSeeMoreButton");
        iTapOnElement("currentConditionsSeeMoreButton");

        iSetThePageAsFrom("CurrentConditionsPage", "/");

        List<String> locators = List.of(
                "pageHeaderText",
                "weatherIconPhareImage"
        );
        iVerifyToElements(locators);
        iVerifyTextInElement("pageHeaderText", "Current Conditions");
        iVerifyTextInElement("weatherIconPhareImage", currentCondition.getWeatherText(), "content-desc");

        //rest of the locator dynamics so we are checking the text with locator otherwise the xpath will be to long and fragile
        // if we can get the element that means the field has the same value in response and if we can't getElement will trigger the Assertions.Fail
        iGetElement("currentTempDynamicText", currentCondition.getTemperature().getImperial().getValue());
        iGetElement("weatherIconPhareDynamicText", currentCondition.getWeatherText());
        iGetElement("unitDynamicText", currentCondition.getTemperature().getImperial().getUnit());
    }

    //@Test
    void test3() {
    }
}