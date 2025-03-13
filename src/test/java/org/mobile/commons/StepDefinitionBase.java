package org.mobile.commons;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.mobile.base.BasePage;
import org.mobile.base.PageManager;
import org.mobile.utils.appium.ElementUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.mobile.base.ThreadLocalManager.getCurrentPageTL;
import static org.mobile.base.ThreadLocalManager.getOSPlatform;
import static org.mobile.config.LogConfig.logDebug;
import static org.mobile.config.LogConfig.logError;

abstract public class StepDefinitionBase {
    BasePage page;
    private final ElementUtil elementUtil = new ElementUtil();

    public void iSetThePageAsFrom(String pageName, String path) {
        PageManager.setPageInstance(pageName, path);
        page = getCurrentPageTL();
    }

    public void iTapOnElement(String key) {
        By locator = page.getLocators().getLocator(key, getOSPlatform());
        WebElement element = elementUtil.getElement(locator);
        elementUtil.tapElement(element);
    }

    public List<WebElement> iGetElements(String key) {
        By locator = page.getLocators().getLocator(key, getOSPlatform());
        return elementUtil.getElements(locator, 20);
    }

    public WebElement iGetElement(String key) {
        By locator = page.getLocators().getLocator(key, getOSPlatform());
        return elementUtil.getElement(locator);
    }

    public WebElement iGetElementWithDynamicField(String key, String changeableField) {
        By locator = page.getLocators().getLocator(key, getOSPlatform());
        logDebug("Dynamic locator: " + locator);
        locator = By.xpath(locator.toString().formatted(changeableField));
        logDebug("Locator updates to: " + locator);
        return elementUtil.getElement(locator);
    }

    public void iWriteIntoElement(String key, String text) {
        By locator = page.getLocators().getLocator(key, getOSPlatform());
        WebElement element = elementUtil.getElement(locator);
        elementUtil.sendKeys(element, text);
    }

    public void iTapEnter(String key) {
        By locator = page.getLocators().getLocator(key, getOSPlatform());
        WebElement element = elementUtil.getElement(locator);
        elementUtil.sendEnter(element);
    }

    public void iWaitToBeVisible(String key) {
        By locator = page.getLocators().getLocator(key, getOSPlatform());
        elementUtil.waitForElementToBeVisible(locator, 20);
    }

    public void iVerifyToSeeElement(String key) {
        By locator = page.getLocators().getLocator(key, getOSPlatform());
        try {
            // If element can be found, it means it is on the page
            WebElement element = elementUtil.getElement(locator);
            String text = element.getAttribute("value");
            logDebug("Element found: [%s]".formatted(locator));
        } catch (Exception e) {
            logError("Element NOT found: [%s]. Error: %s".formatted(locator, e.getMessage()));
            Assertions.fail("Element NOT found: [%s]. Error: %s".formatted(locator, e.getMessage()));
        }
    }

    public void iVerifyToSeeElement(SoftAssertions softAssert, String key) {
        By locator = page.getLocators().getLocator(key, getOSPlatform());
        try {
            // If element can be found, it means it is on the page
            WebElement element = elementUtil.getElement(locator);
            logDebug("Element found: [%s]".formatted(locator));
        } catch (Exception e) {
            logError("Element NOT found: [%s]. Error: %s".formatted(locator, e.getMessage()));
            softAssert.fail("Element NOT found: [%s]. Error: %s".formatted(locator, e.getMessage()));
        }
    }

    public void iVerifyToSeeElements(List<String> keys) {
        SoftAssertions softAssert = new SoftAssertions();
        //to check multiple elements with a list of keys
        keys.forEach(key -> iVerifyToSeeElement(softAssert, key));
        softAssert.assertAll();
    }

    public void iVerifyAttributeInElement(String element, String attribute, String expectedValue) {

    }

    public void iVerifyTextInElement(String key, String expectedText) {
        By locator = page.getLocators().getLocator(key, getOSPlatform());
        WebElement element = elementUtil.getElement(locator);

        String actualText = element.getText();
        Assertions.assertEquals(expectedText, actualText, "Element [%s] text is not matching".formatted(locator));

    }

    public void iVerifyTextInElement(String key, String expectedText, String attributeKey) {
        By locator = page.getLocators().getLocator(key, getOSPlatform());
        WebElement element = elementUtil.getElement(locator);

        String actualText;

        switch (attributeKey.toLowerCase()) {
            case "value":
                actualText = elementUtil.getAttribute(element, "value");
                break;
            case "content-desc":
            case "contentdesc":
            case "contentdescription":
                actualText = elementUtil.getAttribute(element, "contentDescription");
                break;
            case "text":
                actualText = element.getText();
                break;
            default:
                throw new IllegalArgumentException("Unknown attributeKey provided: " + attributeKey);
        }

        Assertions.assertEquals(expectedText, actualText,
                String.format("Element [%s] text mismatch for attribute [%s]!", locator, attributeKey));
    }

}