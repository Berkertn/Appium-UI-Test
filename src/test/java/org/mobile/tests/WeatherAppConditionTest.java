package org.mobile.tests;

import org.api.models.conditions.CurrentCondition;
import org.junit.jupiter.api.*;
import org.mobile.base.TestManagement;

import java.util.List;

import static org.api.steps.RequestSteps.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WeatherAppConditionTest extends TestManagement {

    @Test
    @Order(1)
    @Tag("conditions")
    @Tag("smoke")
    @DisplayName("Weather Conditions For NY UI and API Test")
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
        iWaitToBeVisible("locationResultFirst");
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

}