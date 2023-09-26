package steps;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import helpers.LocatorHelper;
import helpers.PageHelper;
import hooks.Hooks;
import io.cucumber.java.en.When;

/*
    Java class (Click) contains Cucumber step definitions for clicking on web elements,
    including elements that trigger popups, and waiting for the network to be idle.
    It is used for interacting with web pages in Playwright-based automation scripts.
 */

public class ClickDefinitions {

    private Page page;

    @When("^I click the \"(.*)\" (?:button|link|icon|text), on the \"(.*)\" (?:page|menu|sub menu)$")
    public void ClickFunction(String key, String fileName) {

        page = Hooks.getPageManager().getPage(Thread.currentThread().getName());

        String locator = PageHelper.readPageObjects(fileName, key);
        String[] data = LocatorHelper.splitValue(locator);
        String pageObjects = data[0];
        String objectId = data[1];
        Locator elementLocator = LocatorHelper.ElementLocator(page, pageObjects, objectId);

        if (elementLocator.isVisible()) {
            elementLocator.hover();
            elementLocator.click();
            page.waitForLoadState(LoadState.NETWORKIDLE);
        }else {
            System.out.println(elementLocator.innerHTML() +" not visible");
        }
    }

    @When("^I click the \"(.*)\" (?:button|link|icon|text) with popup, on the \"(.*)\" (?:page|menu|sub menu)$")
    public void ClickAndWaitFunction(String key, String fileName) {

        page = Hooks.getPageManager().getPage(Thread.currentThread().getName());

        String locator = PageHelper.readPageObjects(fileName, key);
        String[] data = LocatorHelper.splitValue(locator);
        String pageObjects = data[0];
        String objectId = data[1];
        Locator elementLocator = LocatorHelper.ElementLocator(page, pageObjects, objectId);

        if (elementLocator.isVisible()) {
            page.waitForPopup(() -> {
                elementLocator.hover();
                elementLocator.click();
            });
            page.waitForLoadState(LoadState.NETWORKIDLE);
        }else {
            System.out.println(elementLocator.innerHTML() +" not visible");
        }
    }

}
