package helpers;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
    Java class (LocatorHelper) provides utility methods for working with locators and web elements
    in Playwright-based automation. It offers convenient ways to construct locators, selectors, and
    handle multiple matching elements.
*/

public class LocatorHelper {

    public static String[] splitValue(String input) {
        String[] parts = input.split("=", 2);
        String key = parts[0] + "="; // add the "=" back to data1
        String value = parts[1];
        return new String[]{ key, value };
    }


    public static Locator ElementLocator(Page page,String locatorType, String locatorValue) {

        String locatorId = switch (locatorType.toLowerCase()) {
            case "id=" -> String.format("//*[@id='%s']", locatorValue);
            case "xpath=", "css=" -> locatorValue;
            case "name=" -> String.format("//*[@name='%s']", locatorValue);
            case "linktext=" -> convertLinkTextToXpath(locatorValue);
            case "partiallinktext=" -> convertPartialLinkTextToXpath(locatorValue);
            default -> throw new IllegalArgumentException("Invalid locator type: " + locatorType);
        };
        page.locator(locatorId).waitFor();
        return page.locator(locatorId);
    }

    public static String ElementSelector(Page page,String locatorType, String locatorValue) {

        return switch (locatorType.toLowerCase()) {
            case "id=" -> String.format("//*[@id='%s']", locatorValue);
            case "xpath=", "css=" -> locatorValue;
            case "name=" -> String.format("//*[@name='%s']", locatorValue);
            case "linktext=" -> convertLinkTextToXpath(locatorValue);
            case "partiallinktext=" -> convertPartialLinkTextToXpath(locatorValue);
            default -> throw new IllegalArgumentException("Invalid locator type: " + locatorType);
        };
    }

    public static List<Locator> ElementLocators(Page page, String locatorType, String locatorValue) {

        String locatorId = switch (locatorType.toLowerCase()) {
            case "id=" -> String.format("//*[@id='%s']", locatorValue);
            case "xpath=", "css=" -> locatorValue;
            case "name=" -> String.format("//*[@name='%s']", locatorValue);
            case "linktext=" -> convertLinkTextToXpath(locatorValue);
            case "partiallinktext=" -> convertPartialLinkTextToXpath(locatorValue);
            default -> throw new IllegalArgumentException("Invalid locator type: " + locatorType);
        };

        page.waitForLoadState(LoadState.NETWORKIDLE);

        BrowserHelper.waitForElementVisible(page, locatorId);

        int count = page.locator(locatorId).count();
        if (count > 1) {
            return new ArrayList<>(page.locator(locatorId).all());
        } else if (count == 1) {
            return List.of(page.locator(locatorId).first());
        } else {
            return Collections.emptyList();
        }

    }



    private static String convertPartialLinkTextToXpath(String partialLinkText) {
        return String.format("//a[contains(text(), '%s')]", partialLinkText);
    }

    private static String convertLinkTextToXpath(String linkText) {
        return String.format("//a[text()='%s']", linkText);
    }

}
