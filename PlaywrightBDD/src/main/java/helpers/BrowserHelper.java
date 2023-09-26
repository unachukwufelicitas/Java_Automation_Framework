package helpers;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.JSHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.List;
import java.util.NoSuchElementException;

/*
    Java class (BrowserHelper) provides utility methods for interacting with web elements,
    capturing element screenshots, waiting for elements to become visible, and retrieving element colors.
    It is designed to assist with various tasks related to web automation using the Playwright library.
 */

public class BrowserHelper {

    private static Page page;
    public static void highlightElement(ElementHandle element) {
        page.evaluate("el => { el.style.backgroundColor = 'yellow'; }", element);
        // You can add additional styling to highlight the element as needed
    }

    public static byte[] takeElementScreenshot(ElementHandle element) {
        ElementHandle.ScreenshotOptions options = new ElementHandle.ScreenshotOptions();
        return element.screenshot(options);
    }

    public static void waitForElementVisible(Page page, String locator) {
        page.waitForSelector(locator, new Page.WaitForSelectorOptions().setState(WaitForSelectorState.valueOf("VISIBLE")));
    }


    public static String getLocatorColor(Locator locator, String targetColor) {
        locator.waitFor();
        String colorValue = locator.evaluate("el => window.getComputedStyle(el).getPropertyValue('" + targetColor + "')").toString();
        return convertToHex(colorValue);
    }

    private static String convertToHex(String colorValue) {
        if (colorValue.startsWith("rgb")) {
            String[] colorComponents = colorValue.split("\\s*,\\s*");
            int r = Integer.parseInt(colorComponents[0].substring(4));
            int g = Integer.parseInt(colorComponents[1]);
            int b = Integer.parseInt(colorComponents[2].substring(0, colorComponents[2].length() - 1));
            return String.format("#%02X%02X%02X", r, g, b);
        } else if (colorValue.startsWith("#")) {
            return colorValue;
        }
        return "";
    }

}
