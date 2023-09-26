package steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.RequestOptions;
import helpers.LocatorHelper;
import helpers.PageHelper;
import helpers.PropertyHelper;
import helpers.StorageHelper;
import hooks.Hooks;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.io.IOException;

/*
    Java class (StoreDefinitions) contains Cucumber step definitions for storing values obtained from web elements and
    API responses. It provides a way to capture and reuse data during the execution of
    Playwright-based automation scripts.
*/

public class StoreDefinitions {

    private Page page;
    private APIResponse response;
    @When("^on the \"(.*)\" page, I retrieve the \"(.*)\" text and store as \"(.*)\"$")
    public void StoreValue(String fileName, String key, String storageKey) {

        page = Hooks.getPageManager().getPage(Thread.currentThread().getName());

        String locator = PageHelper.readPageObjects(fileName, key);
        String[] data = LocatorHelper.splitValue(locator);
        String pageObjects = data[0];
        String objectId = data[1];
        Locator elementLocator = LocatorHelper.ElementLocator(page, pageObjects, objectId);
        if (elementLocator.isVisible()) {
            String storageValue = elementLocator.textContent();
            StorageHelper.store(storageKey, storageValue);
        }else {
            System.out.println(elementLocator.innerHTML() +" not visible");
        }

    }

    @When("^I retrieve the \"(.*)\" from the \"(.*)\" and store as \"(.*)\"$")
    public void StoreAPIValue(String key, String route, String storageKey) throws IOException {

        response = Hooks.getApiManager().getResponse((Thread.currentThread().getName()), route, null);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode postJsonResponse = objectMapper.readTree(response.body());
        System.out.println(postJsonResponse.toPrettyString());
        System.out.println(response.status());

        if (key != null) {
            if (postJsonResponse.isArray()) {
                // Iterate over the array elements and check if 'pk' exists in any of them
                for (JsonNode node : postJsonResponse) {
                    JsonNode pkNode = node.get(key);
                    if (pkNode != null) {
                        String userId = pkNode.asText();
                        System.out.println("The " + route + " contains " + userId);
                        StorageHelper.store(storageKey, userId);
                        break; // Exit the loop after finding the first occurrence
                    }
                }
            } else {
                // Handle the situation when the response is not an array
                JsonNode pkNode = postJsonResponse.get(key);
                if (pkNode != null) {
                    String userId = pkNode.asText();
                    System.out.println("The " + route + " contains " + userId);
                    StorageHelper.store(storageKey, userId);
                } else {
                    System.out.println("Key " + key + " not found in the JSON response.");
                    // Handle the situation when the key is not found
                }
            }
        } else {
            System.out.println("pk is null.");
            // Handle the situation when pk is null
        }
    }
}
