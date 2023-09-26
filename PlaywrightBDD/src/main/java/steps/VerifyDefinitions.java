package steps;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.RequestOptions;
import helpers.*;
import hooks.Hooks;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.io.IOException;
import java.util.List;

public class VerifyDefinitions {

    private Page page;
    private APIResponse response;

    @When("^on the \"(.*)\" page, the \"(.*)\" text should be \"(.*)\" as stored$")
    public void VerifyStoreValue(String fileName, String key, String storageKey) {
        page = Hooks.getPageManager().getPage(Thread.currentThread().getName());
        String locator = PageHelper.readPageObjects(fileName, key);
        String[] data = LocatorHelper.splitValue(locator);
        String pageObjects = data[0];
        String objectId = data[1];
        Locator elementLocator = LocatorHelper.ElementLocator(page, pageObjects, objectId);
        if (elementLocator.isVisible()) {
            String actual = elementLocator.textContent();
            String expected = StorageHelper.getValue(storageKey);
            Assert.assertEquals(actual, expected);
        } else {
            System.out.println(elementLocator.innerHTML() + " not visible");
        }
    }

    //on the "Login" page, the "error message" should equal "Invalid username or password!" text
    @When("^on the \"(.*)\" page, the \"(.*)\" should equal the \"(.*)\" text$")
    public void VerifyText(String fileName, String key, String textValue) {
        page = Hooks.getPageManager().getPage(Thread.currentThread().getName());
        String locator = PageHelper.readPageObjects(fileName, key);
        String[] data = LocatorHelper.splitValue(locator);
        String pageObjects = data[0];
        String objectId = data[1];
        Locator elementLocator = LocatorHelper.ElementLocator(page, pageObjects, objectId);
        if (elementLocator.isVisible()) {
            elementLocator.highlight();
            String actual = elementLocator.textContent().trim();
            Assert.assertEquals(actual, textValue.trim());
        } else {
            System.out.println(elementLocator.innerHTML() + " not visible");
        }
    }

    @When("^on the \"(.*)\" page, the \"(.*)\" should not equal the \"(.*)\" text$")
    public void VerifyTextNotPresent(String fileName, String key, String textValue) {
        page = Hooks.getPageManager().getPage(Thread.currentThread().getName());
        String locator = PageHelper.readPageObjects(fileName, key);
        String[] data = LocatorHelper.splitValue(locator);
        String pageObjects = data[0];
        String objectId = data[1];
        Locator elementLocator = LocatorHelper.ElementLocator(page, pageObjects, objectId);
        if (elementLocator.isVisible()) {
            elementLocator.highlight();
            String actual = elementLocator.textContent().trim();
            Assert.assertNotEquals(actual, textValue.trim());
        } else {
            System.out.println(elementLocator.innerHTML() + " not visible");
        }
    }

    /*
     *e.g., "color" for font color, "background-color" for background color
     * on the "home" page, the "background-color" colour of the "Login" element should be "#5cb85c"
     *
     */
    @When("^on the \"(.*)\" page, the \"(.*)\" colour of the  \"(.*)\" element should be \"(.*)\"$")
    public void VerifyLocatorColour(String fileName, String targetColor, String key, String hexExpectedColour) {
        page = Hooks.getPageManager().getPage(Thread.currentThread().getName());
        String locator = PageHelper.readPageObjects(fileName, key);
        String[] data = LocatorHelper.splitValue(locator);
        String pageObjects = data[0];
        String objectId = data[1];
        Locator elementLocator = LocatorHelper.ElementLocator(page, pageObjects, objectId);

        if (elementLocator.isVisible()) {

            String actualColourRGBA = BrowserHelper.getLocatorColor(elementLocator, targetColor);

            Assert.assertEquals(actualColourRGBA, hexExpectedColour.toUpperCase());
        } else {
            System.out.println(elementLocator.innerHTML() + " not visible");
        }
    }

    @When("^the \"(.*)\" response was ok$")
    public void OKFunction(String route) {
        response = Hooks.getApiManager().getResponse((Thread.currentThread().getName()), route, null);
//        System.out.println(response.text());
        Assert.assertTrue(response.ok(), "Response not ok");
    }

    @When("^the \"(.*)\" response contains:$")
    public void containsFunction(String route, DataTable dataTable) {
        response = Hooks.getApiManager().getResponse((Thread.currentThread().getName()), route, null);
        List<List<String>> expectedResponse = dataTable.asLists(String.class);
        String responseBody = response.text();
        System.out.println(responseBody);

        for (List<String> row : expectedResponse) {
            for (String expectedText : row) {
                Assert.assertTrue(responseBody.contains(expectedText));
            }
        }
    }

    @When("^the \"(.*)\" route, with \"(.*)\" contains:$")
    public void containsFunction(String route, String identifier,DataTable dataTable) {
        String localId =  StorageHelper.getValue(identifier);
        RequestOptions auth = RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", PropertyHelper.getProperty("apiToken"));

        response = Hooks.getApiManager().getResponse((Thread.currentThread().getName()), route+"/"+localId, auth);

        List<List<String>> expectedResponse = dataTable.asLists(String.class);
        String responseBody = response.text();
        System.out.println(responseBody);

        for (List<String> row : expectedResponse) {
            for (String expectedText : row) {
                Assert.assertTrue(responseBody.contains(expectedText));
            }
        }
    }

    @When("^the \"(.*)\" route, with \"(.*)\" should not contain \"(.*)\"$")
    public void containsFunction(String route, String identifier, String expectedText) {
        String localId =  StorageHelper.getValue(identifier);
        RequestOptions auth = RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", PropertyHelper.getProperty("apiToken"));

        response = Hooks.getApiManager().getResponse((Thread.currentThread().getName()), route+"/"+localId, auth);
        System.out.println(response.status());
        Assert.assertFalse(response.text().contains(expectedText), localId+" is no longer contained on the body.");


    }

    @When("^the \"(.*)\" response should contain \"(.*)\"$")
    public void containsFunction(String route, String key) {

        String value =  StorageHelper.getValue(key);

        RequestOptions auth = RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", PropertyHelper.getProperty("apiToken"));

        response = Hooks.getApiManager().getResponse((Thread.currentThread().getName()), route+"/"+value, auth);

        System.out.println(response.status());
        System.out.println(response.statusText());
        System.out.println(response.url());
        System.out.println(response.text());
        Assert.assertTrue(response.text().contains(value));

    }

    @When("^the \"(.*)\" response status was \"(.*)\"$")
    public void StatusFunction(String route, String status) {
        response = Hooks.getApiManager().getResponse((Thread.currentThread().getName()), route, null);
        Assert.assertEquals(response.status(), Integer.parseInt(status));
    }
}
