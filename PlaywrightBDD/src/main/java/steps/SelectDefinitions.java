package steps;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.SelectOption;
import helpers.LocatorHelper;
import helpers.PageHelper;
import hooks.Hooks;
import io.cucumber.java.en.When;
import org.testng.Assert;

/*
    Java class (Select) contains Cucumber step definitions for interacting with dropdowns and unordered
    lists on web pages using Playwright. It provides the ability to select options from these UI elements
    during the execution of Playwright-based automation scripts.
*/

public class SelectDefinitions {

    private Page page;

    @When("^I select the \"(.*)\" option from the \"(.*)\", on the \"(.*)\" (?:page|bar|)$")
    public void selectDropdownOption(String optionText, String key,String fileName) {
        page = Hooks.getPageManager().getPage(Thread.currentThread().getName());

        String locator = PageHelper.readPageObjects(fileName, key);
        String[] data = LocatorHelper.splitValue(locator);
        String pageObjects = data[0];
        String objectId = data[1];
        Locator elementLocator = LocatorHelper.ElementLocator(page, pageObjects, objectId);
        if (elementLocator.isVisible()) {
            SelectOption[] options = new SelectOption[] { new SelectOption().setLabel(optionText) };
            elementLocator.selectOption(options);
        }else {
            System.out.println(elementLocator.innerHTML() +" not visible");
        }
    }

    @When("^I select the \"(.*)\" option from the \"(.*)\" list, on the \"(.*)\" (?:page|bar|)$")
    public void selectTextFromUnorderedList(String optionText, String key,String fileName) {
        page = Hooks.getPageManager().getPage(Thread.currentThread().getName());

        String locator = PageHelper.readPageObjects(fileName, key);
        String[] data = LocatorHelper.splitValue(locator);
        String pageObjects = data[0];
        String objectId = data[1];
        Locator[] elementLocators = LocatorHelper.ElementLocators(page, pageObjects, objectId).toArray(new Locator[0]);

        boolean elementClicked = false;
        for (Locator elementLocator : elementLocators) {
            elementLocator.waitFor();
            String actualText = elementLocator.textContent().toLowerCase().trim();
            if (actualText.equalsIgnoreCase(optionText.trim())){
                elementLocator.highlight();
                elementLocator.click();
                System.out.println(actualText+ " have just been selected");
                elementClicked = true;
                break;
            }
        }

        if (!elementClicked) {
            // Assertion failure if no element was clicked
            Assert.fail("None of the elements in the unordered list was clicked");
        }
    }

}
