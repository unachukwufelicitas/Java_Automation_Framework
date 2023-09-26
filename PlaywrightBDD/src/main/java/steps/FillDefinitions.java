package steps;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import helpers.LocatorHelper;
import helpers.PageHelper;
import helpers.PropertyHelper;
import hooks.Hooks;
import io.cucumber.java.en.When;

/*
    Java class (Fill) contains Cucumber step definitions for filling input fields and text areas on web pages using
    Playwright. It provides flexibility to either fill the fields with custom values or predefined admin credentials,
    depending on the scenario.
 */
public class FillDefinitions {
    private Page page;
    @When("^I fill the \"(.*)\" (input|text field|text area) with \"(.*)\", on the \"(.*)\" page$")
    public void FillFunction(String key, String fieldType, String value, String fileName) {
        page = Hooks.getPageManager().getPage(Thread.currentThread().getName());
        String locator = PageHelper.readPageObjects(fileName, key);
        String[] data = LocatorHelper.splitValue(locator);
        String pageObjects = data[0];
        String objectId = data[1];
        Locator elementLocator = LocatorHelper.ElementLocator(page, pageObjects, objectId);
        if (elementLocator.isVisible()) {
            switch (fieldType){
                case "input","text field", "text area" -> {
                    elementLocator.clear();
                    elementLocator.fill(value);
                }
            }
        }else {
            System.out.println(elementLocator.innerHTML() +" not visible");
        }
    }

    @When("^I type the \"(.*)\" (input|text field|text area) with \"(.*)\", on the \"(.*)\" page$")
    public void TypeFunction(String key, String fieldType, String value, String fileName) {
        page = Hooks.getPageManager().getPage(Thread.currentThread().getName());
        String locator = PageHelper.readPageObjects(fileName, key);
        String[] data = LocatorHelper.splitValue(locator);
        String pageObjects = data[0];
        String objectId = data[1];
        Locator elementLocator = LocatorHelper.ElementLocator(page, pageObjects, objectId);
        if (elementLocator.isVisible()) {
            switch (fieldType){
                case "input","text field", "text area" -> {
                    elementLocator.clear();
                    elementLocator.type(value);
                }
            }

        }else {
            System.out.println(elementLocator.innerHTML() +" not visible");
        }
    }

    @When("^I fill the \"(.*)\" field with admin (username|password), on the \"(.*)\" page$")
    public void fillAdminFunction(String key, String fieldType, String fileName) {
        page = Hooks.getPageManager().getPage(Thread.currentThread().getName());
        String locator = PageHelper.readPageObjects(fileName, key);
        String[] data = LocatorHelper.splitValue(locator);
        String pageObjects = data[0];
        String objectId = data[1];
        Locator elementLocator = LocatorHelper.ElementLocator(page, pageObjects, objectId);

        if (elementLocator.isVisible()) {
            switch (fieldType) {
                case "username", "password" -> {
                    elementLocator.clear();
                    elementLocator.type(PropertyHelper.getAuth(fieldType));
                }
                default -> System.out.println("Invalid field type: " + fieldType);
            }
        } else {
            System.out.println(elementLocator.innerHTML() + " not visible");
        }
    }
}
